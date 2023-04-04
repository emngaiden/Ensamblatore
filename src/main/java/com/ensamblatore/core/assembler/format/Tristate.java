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
package com.ensamblatore.core.assembler.format;

import java.io.Serializable;

/**
 *
 * @author emnga
 */
public class Tristate extends Number implements Serializable{

    public int value=2;
    
    public Tristate(int value){
        if(value<0 || value>2)
            throw new NumberFormatException("Invalid initial tristate state");
        this.value=value;
    }
    
    public Tristate(boolean value){
        if(value)
            this.value=1;
        else
            this.value=0;
    }
    
    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return (long)(value);
    }

    @Override
    public float floatValue() {
        return (float)value;
    }

    @Override
    public double doubleValue() {
        return value+0.0;
    }
    
    public boolean booleanValue(){
        return value==1;
    }
    
    @Override
    public String toString(){
        switch (this.value) {
            case 1:
                return "1";
            case 0:
                return "0";
            default:
                return "X";
        }
    }
    
}
