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
package com.ensamblatore.core.assembler;

import com.ensamblatore.core.architecture.Word;
import com.ensamblatore.exceptions.ReferenceNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author emnga
 */
public class ListingFile implements Serializable{
    
    ArrayList<Variable> variables;
    
    public ListingFile(){
        this.variables=new ArrayList<>();
    }
    
    public Variable findVariable(String name){
        for(Variable var: this.variables)
            if(var.name.equals(name))
                return var;
        throw new ReferenceNotFoundException("Variable Table",name);
    }
    
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("n")
                .append('\t')
                .append("index")
                .append('\t')
                .append("name")
                .append('\t')
                .append("value")
                .append('\t')
                .append("words")
                .append('\n');
        for(int i=0; i<this.variables.size(); i++){
            Variable var=this.variables.get(i);
            sb.append(i+1)
                    .append(':')
                    .append('\t')
                    .append(var.positionInMemory)
                    .append('\t')
                    .append(var.name)
                    .append('\t')
                    .append(var.value)
                    .append('\t')
                    .append(var.words);
            if(i!=this.variables.size()-1){
                sb.append('\n');
            }
        }
        return sb.toString();
    }
    
    public boolean add(String name, Word value, int position,int words){
        if (!this.variables.stream().noneMatch((var) -> (var.getName().equals(name) || var.getPositionInMemory()==position))) {
            return false;
        }
        return this.variables.add(new Variable(name,value,words,position));
    }
    
    public boolean addName(String name){
        if (!this.variables.stream().noneMatch((var) -> (var.name.equals(name)))) {
            return false;
        }
        return this.variables.add(new Variable(name));
    }
    
    public boolean addValue(String name, Word value){
        for(Variable var: this.variables){
            if(var.name.equals(name)){
                var.value=value;
                return true;
            }
        }
        return false;
    }
    
    class Variable implements Serializable{
        private String name;
        private Word value;
        private int positionInMemory;
        private int words;

        public Variable(String name, Word value,int words, int position) {
            this.name = name;
            this.value = value;
            this.positionInMemory=position;
            this.words=words;
        }

        public Variable(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Word getValue() {
            return value;
        }

        public void setValue(Word value) {
            this.value = value;
        }

        public int getPositionInMemory() {
            return positionInMemory;
        }

        public void setPositionInMemory(int positionInMemory) {
            this.positionInMemory = positionInMemory;
        }

        public int getWords() {
            return words;
        }

        public void setWords(int words) {
            this.words = words;
        }
        
        
        
        
    }
    
}
