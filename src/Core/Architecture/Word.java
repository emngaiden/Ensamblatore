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

import java.util.Random;
import Exceptions.WordSizeException;
import java.io.Serializable;
import java.util.Arrays;

/**
 * This class is a representation of a binary array in memory.
 * @author emnga
 */
public class Word implements Serializable{
    public int size;
    public boolean[] bits;
    public boolean isReversed=false;
    public static enum Operations{AND,OR,NOT,XOR};
    
    /**
     * 
     * @param size
     */
    public Word(int size){
        this.size=size;
        this.bits=new boolean[size];
    }
    
    public void trimToSize(){
        int trim=0;
        boolean wasReversed=this.isReversed==true;
        if (!this.isReversed){
            this.flip();
        }
        for(int i=0; i<this.size; i++){
            if(!this.bits[i]){
                trim++;
            }
            else{
                break;
            }
        }
        if(trim>0){
            if(trim==this.size){
                this.size=1;
                this.bits=new boolean[]{false};
                return;
            }
            int newSize=this.size-trim;
            boolean[] newBits=new boolean[newSize];
            for(int i=0; i<newSize; i++){
                newBits[i]=this.bits[i+trim];
            }
            this.size=newSize;
            this.bits=newBits;
        }
        
        if(wasReversed){
            this.flip();
        }
    }
    public final void flip(){
        boolean[] newBits=new boolean[this.size];
        for(int i=0; i<this.size; i++){
            newBits[i]=this.bits[this.size-i-1];
        }
        this.isReversed=!this.isReversed;
        this.bits=newBits;
    }
    public Word(int size,boolean reverse, boolean... values){
        this.size=size;
        this.bits=new boolean[size];
        for(int i=0; i<values.length; i++){
            try{
                this.bits[i]=values[i];
            }
            catch(IndexOutOfBoundsException e){
                break;
            }
        }
        if(reverse){
            this.flip();
        }
    }
    
    public Word(int size, int... values){
        this.size=size;
        this.bits=new boolean[size];
        try{
            int counter=0;
        for(int i=values.length-1; i>=0; i--){
            bits[counter] = values[i]>=1;
            counter++;
        }
        }catch(IndexOutOfBoundsException e){
            throw(e);
        }
    }
    
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        for(int i=this.size-1; i>=0; i--){
            if(bits[i])
                sb.append("1");
            else{
                sb.append("0");
            }
        }
        return sb.toString();
    }
    
    public static Word generateRandomWord(int wordSize){
        boolean[] data= new boolean[wordSize];
        for(int i=0; i<data.length; i++){
            data[i]=new Random().nextBoolean();
        }
        Word ret=new Word(wordSize,false,data);
        return ret;
    }
    
    public Word operation(Word.Operations operation, Word argument){
        switch(operation){
            case AND:
                return and(argument);
            case NOT:
                return not();
            case OR:
                return or(argument);
            case XOR:
                return xor(argument);
            default:
                return null;
        }
    }
    
    private Word not(){
        Word ret=new Word(this.size);
        for(int i=0; i<this.size; i++){
            ret.bits[i]=Word.not(this.bits[i]);
        }
        return ret;
    }
    
    private Word and(Word word){
        if(word.size!=this.size){
            throw new WordSizeException("Words in and operation have different sizes");
        }
        Word ret=new Word(this.size);
        for(int i=0; i<word.size; i++){
            ret.bits[i]=Word.and(this.bits[i], word.bits[i]);
        }
        return ret;
    }
    
    private Word or(Word word){
        if(word.size!=this.size){
            throw new WordSizeException("Words in or operation have different sizes");
        }
        Word ret=new Word(this.size);
        for(int i=0; i<word.size; i++){
            ret.bits[i]=Word.or(this.bits[i], word.bits[i]);
        }
        return ret;
    }
    
    private Word xor(Word word){
        if(word.size!=this.size){
            throw new WordSizeException("Words in xor operation have different sizes");
        }
        Word ret=new Word(this.size);
        for(int i=0; i<word.size; i++){
            ret.bits[i]=Word.xor(this.bits[i], word.bits[i]);
        }
        return ret;
    }
    
    private static boolean and(boolean i, boolean i2){
        return (i == true && i == i2); 
    }
    
    private static boolean or(boolean i, boolean i2){
        return (i == true || i2 == true);
    }
    
    private static boolean not(boolean i){
        return !i;
    }
    
    private static boolean xor(boolean i, boolean i2){
        return(i!=i2);
    }
    
    public static Word parseWord(String toParse){
        boolean[] bool=new boolean[toParse.length()];
        for(int i=toParse.length()-1; i>=0; i--){
            bool[i]=toParse.charAt(i)=='1';
        }
        return new Word(toParse.length(),true,bool);
    }
    
    public static boolean parseBoolean(String toParse){
        return toParse.contains("true")||toParse.equals("1");
    }
    
    /*public void trimToSize(){
        int toRest=0;
        for(int i=this.bits.length-1; i>=0; i--){
            if(!this.bits[i]){
                this.bits=Arrays.copyOfRange(bits, 0, i);
                toRest++;
            }
            else{
                break;
            }
        }
        this.size=this.size-toRest;
    }*/
}
