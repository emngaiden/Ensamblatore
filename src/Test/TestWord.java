/*
 * Copyright (C) 2018 emnga
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
package Test;

import Core.Architecture.Word;

/**
 *
 * @author emnga
 */
public class TestWord {
    public static void main(String[] args) {
        Word a=new Word(8,true, true,false,true,true);
        Word b=new Word(8,false, true,false,true,true);
        Word c=new Word(8,true);
        System.out.println("One");
        System.out.println(a.toString());
        System.out.println(b.toString());
        a.trimToSize();
        b.trimToSize();
        c.trimToSize();
        System.out.println("Two");
        System.out.println(a.toString());
        System.out.println(b.toString());
        System.out.println(c.toString());
    }
}
