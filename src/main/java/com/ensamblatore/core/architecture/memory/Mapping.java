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
package com.ensamblatore.core.architecture.memory;

import com.ensamblatore.core.architecture.Sector;
import com.ensamblatore.core.architecture.SectorComparator;
import com.ensamblatore.exceptions.SectorNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author emnga
 */
public class Mapping implements Serializable{
    ArrayList<Sector> sectors;
    
    public Mapping(){
        sectors=new ArrayList<>();
    }

    public ArrayList<Sector> getSectors() {
        return sectors;
    }

    public void setSectors(ArrayList<Sector> sectors) {
        this.sectors = sectors;
    }
    
    public boolean addSector(Sector sectorr){
        int start=sectorr.start;
        int finish=sectorr.finish;
        String name=sectorr.name;
        for(Sector sector: sectors){
            if((start>=sector.start && finish<=sector.finish)||
                    (start<=sector.start && finish>= sector.start)||
                    (start<=sector.finish && finish>=sector.finish)|| 
                    (finish<start) || (name.equals(sector.name))){
                return false;
            }
        }
        this.sectors.add(new Sector(start,finish,name));
        this.sectors.sort(new SectorComparator());
        return true;
    }
    
    public boolean addSector(int start, int finish, String name){
        for(Sector sector: sectors){
            if((start>=sector.start && finish<=sector.finish)||
                    (start<=sector.start && finish>= sector.start)||
                    (start<=sector.finish && finish>=sector.finish)|| 
                    (finish<start) || (name.equals(sector.name))){
                return false;
            }
        }
        this.sectors.add(new Sector(start,finish,name));
        this.sectors.sort(new SectorComparator());
        return true;
    }
    
    public boolean removeSector(String name){
        for(int i=0; i<sectors.size(); i++){
            if(sectors.get(i).name.equals(name)){
                this.sectors.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public Sector findSector(String name){
        for(Sector sector: sectors){
            if(name.equals(sector.name)){
                return sector;
            }
        }
        throw new SectorNotFoundException("Sector "+name+" not found on mapping");
    }
    
    public static Sector parseSector(String p){
        String[] splited=p.split(",");
        String name=splited[0];
        int from=Integer.parseInt(splited[1]);
        int to=Integer.parseInt(splited[2]);
        return new Sector(from, to, name);
    }
}
