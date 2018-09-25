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
package Core.Assembler.Format;

import Core.Assembler.Instruction.InstructionFormat;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author emnga
 */
public class Format implements Serializable{
    public int size;
    public Tristate[] data;

    public Format(int size) {
        this.size = size;
        this.data = new Tristate[size];
        for(int i=0; i<this.size; i++){
            this.data[i]=new Tristate(2);
        }
    }
    
    public Format(int size, int... values){
        this.size=size;
        this.data=new Tristate[size];
        int counter=0;
        try{
            for(int i=values.length-1; i>=0; i--){
                this.data[counter]=new Tristate(values[i]);
                counter++;
            }
        }catch(IndexOutOfBoundsException e){
            System.out.println("Size excedeed in format");
        }
    }
    
    public Format(Format format){
        this.data=format.data.clone();
        this.size=format.size;
    }

    public Format(InstructionFormat format) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString(){
        StringBuilder sb= new StringBuilder();
        sb.append(Arrays.toString(data));
        return sb.toString();
    }
}
