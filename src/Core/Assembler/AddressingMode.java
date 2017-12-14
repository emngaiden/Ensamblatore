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
package Core.Assembler;

import Core.Architecture.Word;
import Core.CPU;
import java.io.Serializable;

/**
 *
 * @author emnga
 */
public abstract class AddressingMode implements Serializable{
    
    public static final AddressingMode INMEDIATO=new AddressingMode("Inmediato") {
        @Override
        public Word adress(String toAdress) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
            ,INDEXADO=new AddressingMode("Indexado") {
        @Override
        public Word adress(String toAdress) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
            ,SIMBOLICO=new AddressingMode("Simbólico") {
        @Override
        public Word adress(String toAdress) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
            ,ABSOLUTO=new AddressingMode("Absoluto") {
        @Override
        public Word adress(String toAdress) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
            ,REGISTROINDIRECTO=new AddressingMode("Registro indirecto") {
        @Override
        public Word adress(String toAdress) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
            ,AUTOINCREMENTOINDIRECTO=new AddressingMode("Autoincremento indirecto") {
        @Override
        public Word adress(String toAdress) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
            ,REGISTRO=new AddressingMode("Registro") {
        @Override
        public Word adress(String toAdress) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    };
    private final String identifier;
    public static CPU parent;
    public abstract Word adress(String toAdress);
    
    public AddressingMode(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
    
    public static void init(CPU parent){
        AddressingMode.parent=parent;
    }
    
    public AddressingMode(AddressingMode mode){
        this.identifier=mode.identifier;
    }
}
