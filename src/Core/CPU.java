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
package Core;

import Core.Architecture.Architecture;
import Core.Assembler.Assembler;
import java.io.Serializable;

/**
 * This class represents a whole CPU with its Architecture and its Assembler.
 * You can use this class constructors directly, but its better to use
 * CPUFactory class
 * @author emnga
 * @see Architecture
 * @see Assembler
 */
public class CPU implements Serializable{
    public String name="";
    private int versionCode=0;
    private String versionName="1.0";
    private String versionDescription="";
    public Architecture architecture;
    private Assembler assembler;
    
    /**
     * Creates an empty CPU (without architecture), only with a string to 
     * identify it.
     * @param name the name of the CPU
     */
    
    public CPU(String name){
        this.name=name;
        this.assembler=new Assembler();
        this.architecture=new Architecture();
    }
    
    public CPU(){
        this.assembler=new Assembler();
        this.architecture=new Architecture();
    }
    
    /**
     * Creates a CPU with the specified architecture. Use ArchitectureFactory
     * to create an Architecture object
     * @param name the name of the CPU
     * @param architecture the architecture for the CPU
     */
    public CPU(String name, Architecture architecture) {
        this(name);
        this.architecture = architecture;
    }
    
    /**
     * Overwrites the existing architecture (if exists) for a new architecture.
     * @param architecture the new architecture
     */
    public void setArchitecture(Architecture architecture){
        this.architecture=architecture;
    }

    public String getName() {
        return name;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionDescription() {
        return versionDescription;
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setVersionDescription(String versionDescription) {
        this.versionDescription = versionDescription;
    }

    public Assembler getAssembler() {
        return assembler;
    }

    public void setAssembler(Assembler assembler) {
        this.assembler = assembler;
    }
    
    public CPU(CPU cpu){
        this.architecture=cpu.getArchitecture();
        this.assembler=cpu.getAssembler();
        this.name=cpu.name;
        this.versionCode=cpu.versionCode;
        this.versionDescription=cpu.versionDescription;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
