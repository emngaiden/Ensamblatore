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
package com.ensamblatore.core.architecture;

import java.io.Serializable;

/**
 *
 * @author emnga
 */
public class Sector implements Serializable{
    public int start;
    public int finish;
    public String name;
    public int size;

    public Sector(int start, int finish, String name) {
        this.start = start;
        this.finish = finish;
        this.name = name;
        this.size=(finish-start)+1;
    }
    
}
