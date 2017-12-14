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
package Exceptions;

import Core.Architecture.Word;

/**
 *
 * @author emnga
 */
public class WordSizeException extends RuntimeException{

    public Word word;
    public String sectorName;
    public int size1, size2;

    public WordSizeException(Word word, String sectorName,int size1, int size2, String name) {
        super("Word "+word.toString()+" to big for sector "+sectorName+". "+size1+" to "+size2+" in "+name);
        this.word = word;
        this.sectorName = sectorName;
        this.size1=size1;
        this.size2=size2;
    }
    
    public WordSizeException(Word word, String sectorName,int size1, int size2) {
        super("Word "+word.toString()+" to big for sector "+sectorName+". "+size1+" to "+size2);
        this.word = word;
        this.sectorName = sectorName;
        this.size1=size1;
        this.size2=size2;
    }
    
    public WordSizeException() {
    }

    public WordSizeException(String string) {
        super(string);
    }
    
}
