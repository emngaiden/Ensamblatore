/*
 * Copyright (C) 2017 emnga
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package Core.Architecture;

import Core.Architecture.Memory.Memory;
import Core.CPU;
import Exceptions.IndexOutOfMemoryException;
import Exceptions.NoMemoryFoundException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

/**
 * This class represents the architecture of a CPU, containing all of his
 * basic properties and characteristics essential to the normal way of a 
 * CPU work cycle
 * @author emnga
 */
public class Architecture implements Serializable{

    public static String DATAMEMORYNAME = "DATA";
    public static String PROGRAMMEMORYNAME= "PROGRAM";
    public static String BIGENDIANNAME= "BIG";
    public static String LITTLEENDIANNAME= "LITTLE";
    private static final String[] DEFAULTNAMES=new String[]{
        "DATA",
        "PROGRAM",
        "BIG",
        "LITTLE"
    };

    public Memory[] getMemory() {
        return memory;
    }
    
    public Endianess getEndianism() {
        return endianism;
    }

    
    public static enum Endianess{BIG,LITTLE};
    private int wordSize=0;
    private int procesorBits=0;
    private Memory[] memory;
    private int PCincrement=0;
    private Endianess endianism = Endianess.BIG;
    private int programInitialRegister=0;
    private int dataInitialRegister=0;
    private CPU parent;
    private String[] staticData;
    
    
    /**
     * Creates a new architecture, with the memories ands the desired increment
     * on the program counter
     * @param memory memories of the architecture
     * @param PCIncrement program counter increment
     */
    public Architecture(Memory[] memory, int PCIncrement,CPU parent) {
        this.memory = memory;
        for(Memory mem: this.memory)
            mem.setParent(this);
        this.PCincrement=PCIncrement;
        this.parent=parent;
    }
    
    
    /**
     * Creates a new architecture, with the memories, the desired increment
     * on the program counter, and a custom word size
     * @param wordSize the word size of the architecture
     * @param memory memories of the architecture
     * @param PCIncrement program counter increment
     */
    
    public Architecture(){
        this.memory=new Memory[0];
    }
    
    /**
     * Add a new memory to the architecture, accessed by an index.
     * @param memory
     * @return
     */
    public boolean addMemory(Memory memory){
        int newSize=this.memory.length+1;
        memory.setParent(this);
        Memory[] newMemory=new Memory[newSize];
        System.arraycopy(this.memory, 0, newMemory, 0, this.memory.length);
        newMemory[newSize-1]=memory;
        this.memory=newMemory;
        return true;
    }

    public int getWordSize() {
        return wordSize;
    }

    public void setWordSize(int wordSize) {
        this.wordSize = wordSize;
    }

    public int getDataInitialRegister() {
        return dataInitialRegister;
    }

    public void setDataInitialRegister(int dataInitialRegister) {
        this.dataInitialRegister = dataInitialRegister;
    }

    public CPU getParent() {
        return parent;
    }
    
    public String[] getMemoriesNames(){
        String[] ret=new String[this.getMemoriesCount()];
        for(int i=0; i<ret.length; i++){
            ret[i]=this.memory[i].getIdentifier();
        }
        return ret;
    }
    
    public int getProcesorBits() {
        return procesorBits;
    }

    public void setProcesorBits(int procesorBits) {
        this.procesorBits = procesorBits;
    }
    
    public int getProgramInitialRegister() {
        return programInitialRegister;
    }

    public void setProgramInitialRegister(int programInitialRegister) {
        this.programInitialRegister = programInitialRegister;
    }
    
    /**
     * Replaces the current memories with the new ones specified
     * @param memory the new memories
     */
    public void setMemory(Memory[] memory){
        this.memory=memory;
    }

    public void setEndianism(Endianess endianism) {
        this.endianism = endianism;
    }
    
    public Memory getMemoryFromIdentifier(String identifier){
        for(Memory mem: this.memory){
            if (mem.getIdentifier().equals(identifier)){
                return mem;
            }
        }
        return null;
    }
    
    public int getMemoryIndexFromIdentifier(String identifier){
        for(int i=0; i<this.getMemoriesCount(); i++){
            if(this.memory[i].getIdentifier().equals(identifier)){
                return i;
            }
        }
        return -1;
    }
    
    public Word[] readMemory(int memory, int initialRegister){
        Word[] ret;
        Memory readfrom=null;
        try{
            readfrom=this.memory[memory];
        }
        catch(IndexOutOfBoundsException e){
            throw new NoMemoryFoundException("No memory found with index "+memory);
        }
        if(initialRegister<0 || initialRegister>=readfrom.getSizeInBytes()){
            throw new IndexOutOfMemoryException("index: "+initialRegister+", memory size: "+readfrom.getSizeInBytes());
        }
        int newSize=(readfrom.sizeInBytes-(initialRegister+1))/this.PCincrement;
        ret=new Word[newSize];
        for(int i=0; i<newSize; i++){
            ret[i]=readfrom.getWord(initialRegister+(this.PCincrement*i));
        }
        return ret;
    }
    
    public int getMemoriesCount(){
        return this.memory.length;
    }
    
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("Architecture").append("\n");
        sb.append("Word Size: ").append(this.wordSize).append("\n");
        sb.append("Program Counter Increment: ").append(this.PCincrement).append("\n");
        sb.append("Memories: ").append(this.memory.length).append("\n");
        sb.append("Endianess: ").append(this.endianism.toString());
        return sb.toString();
        
    }
    
    public int[] initMemoryCounters() {
        int[] ret=new int[this.getMemoriesCount()+1];
        if(this.getMemoriesCount()>1){
            for(int i=0; i<this.getMemoriesCount(); i++){
                if(this.memory[i].getIdentifier().equals(Architecture.DATAMEMORYNAME)){
                    this.memory[i].id=i;
                    ret[i]=this.dataInitialRegister;
                }
                else if(this.memory[i].getIdentifier().equals(Architecture.PROGRAMMEMORYNAME)){
                    ret[i]=this.programInitialRegister;
                    this.memory[i].id=i;
                }
                else{
                    ret[i]=0;
                    this.memory[i].id=i;
                }
            }
        }
        else{
            this.memory[0].id=0;
            ret[0]=this.programInitialRegister;
            ret[1]=this.dataInitialRegister;
        }
        return ret;
    }
    
    public String memoriesToString(){
        StringBuilder sb=new StringBuilder();
        for (Memory memory1 : this.memory) {
            sb.append(memory1.getIdentifier()).append("\n");
            sb.append(memory1.toString());
        }
        return sb.toString();
    }
    
    public void prepareForSerialization(){
        this.staticData=new String[4];
        this.staticData[0]=Architecture.DATAMEMORYNAME;
        this.staticData[1]=Architecture.PROGRAMMEMORYNAME;
        this.staticData[2]=Architecture.BIGENDIANNAME;
        this.staticData[3]=Architecture.LITTLEENDIANNAME;
    }
    
    public String compareDifferences(Architecture comparison){
        StringBuilder sb=new StringBuilder();
        if(this.PCincrement!=comparison.PCincrement){
            sb.append('\n');
            sb.append(createMessage("Program counter", this.PCincrement, comparison.PCincrement));
        }
        if(this.dataInitialRegister!=comparison.dataInitialRegister){
            sb.append('\n');
            sb.append(createMessage("Data initial register", this.dataInitialRegister, comparison.dataInitialRegister));
        }
        if(this.endianism!=comparison.endianism){
            sb.append('\n');
            sb.append(createMessage("Endianism", this.endianism, comparison.endianism));
        }
        if(this.memory.equals(comparison.endianism)){
            sb.append('\n');
            sb.append("Memories");
        }
        if(this.procesorBits!=comparison.procesorBits){
            sb.append('\n');
            sb.append(createMessage("Processor's bits", this.procesorBits, comparison.procesorBits));
        }
        if(this.programInitialRegister!=comparison.programInitialRegister){
            sb.append('\n');
            sb.append(createMessage("Program initial register", this.programInitialRegister, comparison.programInitialRegister));
        }
        if(this.wordSize!=comparison.wordSize){
            sb.append('\n');
            sb.append(createMessage("Word size", this.wordSize, comparison.wordSize));
        }
        return sb.toString();
    }
    
    private String createMessage(String name, Object oldData, Object newData){
        StringBuilder sb=new StringBuilder(name);
        sb.append(": ");
        sb.append("old = ");
        sb.append(oldData.toString());
        sb.append(", ");
        sb.append("new = ");
        sb.append(newData.toString());
        return sb.toString();
    }
    
    public static void restoreDefaultNames(){
        Architecture.DATAMEMORYNAME=Architecture.DEFAULTNAMES[0];
        Architecture.PROGRAMMEMORYNAME=Architecture.DEFAULTNAMES[1];
        Architecture.BIGENDIANNAME=Architecture.DEFAULTNAMES[2];
        Architecture.LITTLEENDIANNAME=Architecture.DEFAULTNAMES[3];
    }
    
    private Architecture(Architecture serialized, CPU parent, boolean modifyNames){
        this.parent=parent;
        this.PCincrement=serialized.PCincrement;
        this.dataInitialRegister=serialized.dataInitialRegister;
        this.endianism=serialized.endianism;
        this.memory=serialized.memory.clone();
        this.procesorBits=serialized.procesorBits;
        this.programInitialRegister=serialized.programInitialRegister;
        this.wordSize=serialized.wordSize;
        if(modifyNames){
            Architecture.DATAMEMORYNAME=serialized.staticData[0];
            Architecture.PROGRAMMEMORYNAME=serialized.staticData[1];
            Architecture.BIGENDIANNAME=serialized.staticData[2];
            Architecture.LITTLEENDIANNAME=serialized.staticData[3];
        }
    }

    public int getPCincrement() {
        return PCincrement;
    }

    public void setPCincrement(int PCincrement) {
        this.PCincrement = PCincrement;
    }
    
    public static String[] getDefaultNames(){
        return Architecture.DEFAULTNAMES;
    }
    
    public void sortMemories(){
        Arrays.sort(memory, new MemoriesComparator());
    }
    
    public class MemoriesComparator implements Comparator<Memory>{

        @Override
        public int compare(Memory t, Memory t1) {
            if(t.getIdentifier().equals(Architecture.DATAMEMORYNAME) && t1.getIdentifier().equals(Architecture.PROGRAMMEMORYNAME)){
                return 1;
            }
            else if(t.getIdentifier().equals(Architecture.PROGRAMMEMORYNAME) && t1.getIdentifier().equals(Architecture.DATAMEMORYNAME)){
                return -1;
            }
            else if(t.getIdentifier().equals(Architecture.DATAMEMORYNAME) || t.getIdentifier().equals(Architecture.PROGRAMMEMORYNAME)){
                return -1;
            }
            else{
                return t.getIdentifier().compareTo(t1.getIdentifier());
            }
        }
        
    }
}
