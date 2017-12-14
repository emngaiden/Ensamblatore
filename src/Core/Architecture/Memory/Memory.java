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
package Core.Architecture.Memory;

import Core.Architecture.Sector;
import Core.Architecture.Architecture;
import Core.Architecture.Word;
import Exceptions.WordSizeException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author emnga
 */
public class Memory implements Serializable{

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Architecture getParent() {
        return parent;
    }

    public void setParent(Architecture parent) {
        this.parent = parent;
    }

    public static enum MemoryDistribution {
        SINGLE, MULTIPLE
    };
    public int sizeInBytes;
    public Mapping mapping;
    public Word[] data;
    public int registerSize;
    private String identifier;
    private Architecture parent;
    public int id;
    public static String DEFAULTREGISTERACCESNAME="R";

    public final void init() {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = new Word(this.registerSize);
        }
    }

    public Memory(int sizeInBytes, int registerSize, String identifier) {
        this.sizeInBytes = sizeInBytes;
        this.registerSize = registerSize;
        this.identifier = identifier;
        this.data = new Word[sizeInBytes];
        this.mapping = new Mapping();
        this.init();

    }

    public Memory(int sizeInBytes, int regiterSize, String identifier, Architecture parent) {
        this(sizeInBytes, regiterSize, identifier);
        this.parent = parent;
    }

    public int getRegisterSize() {
        return registerSize;
    }

    public void setRegisterSize(int registerSize) {
        this.registerSize = registerSize;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(int sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public Mapping getMapping() {
        return mapping;
    }

    public void setMapping(Mapping mapping) {
        this.mapping = mapping;
    }

    public Word[] getData() {
        return data;
    }

    public void setData(Word[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int[] starts = new int[this.mapping.sectors.size()], ends = new int[this.mapping.sectors.size()];
        for (int i = 0; i < this.mapping.sectors.size(); i++) {
            Sector sector = this.mapping.sectors.get(i);
            starts[i] = sector.start;
            ends[i] = sector.finish;
        }
        for (int i = 0; i <= this.data.length; i++) {
            for (int j = 0; j < starts.length; j++) {
                if (starts[j] == i) {
                    sb.append("\t").append("--").append(this.mapping.sectors.get(j).name).append("\n");
                    break;
                } else if (ends[j] + 1 == i) {
                    sb.append("\t").append(this.mapping.sectors.get(j).name).append("--").append("\n");
                    break;
                }
            }
            try {
                Word dat = this.data[i];
                sb.append(i).append(": ").append('\t').append(dat).append('\n');
            } catch (IndexOutOfBoundsException e) {
                System.out.println("IndexOutOfBoundsException catched on: " + this.getClass().getName());
            }
        }
        return sb.toString();
    }

    public Word getWord(int position) {
        return this.data[position];
    }

    public Word getWord(int multiplier, int position, Architecture.Endianess endianism) {
        int newSize = this.registerSize * multiplier;
        boolean[] formedData;
        if (endianism.equals(Architecture.Endianess.BIG)) {
            boolean[][] datas = new boolean[multiplier][];
            for (int i = 0; i < multiplier; i++) {
                datas[i] = this.data[position + i].bits;
            }
            formedData = mergeWords(datas, newSize);
        } else {
            boolean[][] datas = new boolean[multiplier][];
            for (int i = 0; i < multiplier; i++) {
                datas[i] = this.data[position + (multiplier - i - 1)].bits;
            }
            formedData = mergeWords(datas, newSize);
        }
        return new Word(formedData.length, false, formedData);
    }

    public Word getWordFromSector(String sectorName, int index) {
        Sector found = this.mapping.findSector(sectorName);
        if (index > found.size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", sector size: " + found.size);
        }
        return getWord(found.start + index);
    }

    public Word getWordFromSector(int multiplier, String sectorName, int index, Architecture.Endianess endianism) {
        Sector found = this.mapping.findSector(sectorName);
        if (index > found.size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", sector size: " + found.size);
        }
        return getWord(multiplier, found.start + index, endianism);
    }

    public int setWord(int position, Word value) {
        if (value.size > this.registerSize) {
            double multiplieraux = (value.size * 1.0) / (1.0 * this.registerSize);
            int multiplier = multiplieraux % 1 != 0 ? (int) multiplieraux + 1 : (int) multiplieraux;
            return this.parent.getPCincrement()==0?this.parent.getPCincrement():setWord(multiplier, position, value, this.parent.getEndianism());
        } else if (value.size < this.registerSize) {
            boolean[] newData = new boolean[this.registerSize];
            System.arraycopy(value.bits, 0, newData, 0, value.bits.length);
            this.data[position] = new Word(this.registerSize, false, newData);
            return this.parent.getPCincrement()==0?this.parent.getPCincrement():1;
        } else {
            this.data[position] = value;
            return this.parent.getPCincrement()==0?this.parent.getPCincrement():1;
        }
    }

    public int setWord(int multiplier, int position, Word value, Architecture.Endianess endianism) {
        boolean[][] datas = unmergeWords(value, multiplier);
        if (endianism.equals(Architecture.Endianess.BIG)) {
            for (int i = 0; i < multiplier; i++) {
                setWord(position + i, new Word(this.registerSize, true, datas[i]));
            }
        } else {
            for (int i = 0; i < multiplier; i++) {
                setWord(position + (multiplier - i - 1), new Word(this.registerSize, true, datas[i]));
            }
        }
        return multiplier;
    }

    public boolean setWordFromSector(String sectorName, int index, Word value) {
        Sector found = this.mapping.findSector(sectorName);
        if (index > found.size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", sector size: " + found.size);
        }
        if (value.size != this.registerSize) {
            return false;
        }
        this.data[index + found.start] = value;
        return true;
    }

    private boolean[] mergeWords(boolean[][] datas, int newSize) {
        boolean[] ret = new boolean[newSize];
        int counter = 0;
        for (int i = 0; i < datas.length / 2; i++) {
            boolean[] temp = datas[i];
            datas[i] = datas[datas.length - i - 1];
            datas[datas.length - i - 1] = temp;
        }
        for (boolean[] oldData : datas) {
            if (oldData.length != this.registerSize) {
                throw new WordSizeException("Word sizes does not match while merging. Size: " + oldData.length + ", allowed size: " + this.registerSize);
            }
            for (int i = 0; i < oldData.length; i++) {
                ret[counter] = oldData[i];
                counter++;
            }
        }
        for (int i = 0; i < ret.length / 2; i++) {
            boolean temp = ret[i];
            ret[i] = ret[ret.length - i - 1];
            ret[ret.length - i - 1] = temp;
        }
        return ret;
    }

    private boolean[][] unmergeWords(Word value, int multiplier) {
        boolean[][] ret = new boolean[multiplier][this.registerSize];
        int counter = 0;
        for (int i = 0; i < multiplier; i++) {
            for (int j = 0; j < this.registerSize; j++) {
                try {
                    ret[i][j] = value.bits[counter];
                    counter++;
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
        }
        for (boolean[] ret1 : ret) {
            for (int i = 0; i < ret1.length / 2; i++) {
                boolean temp = ret1[i];
                ret1[i] = ret1[ret1.length - i - 1];
                ret1[ret1.length - i - 1] = temp;
            }
        }
        for (int i = 0; i < ret.length / 2; i++) {
            boolean[] temp = ret[i];
            ret[i] = ret[ret.length - i - 1];
            ret[ret.length - i - 1] = temp;
        }
        return ret;
    }

    public String belongsTo(int index) {
        for (Sector sector : this.mapping.sectors) {
            if (sector.start <= index && index <= sector.finish) {
                return sector.name;
            }
        }
        return "";
    }

    public void clearAll() {
        for (int i = 0; i < sizeInBytes; i++) {
            this.data[i] = new Word(this.registerSize);
        }
        this.mapping = new Mapping();
    }
    
    public void clearData() {
        for (int i = 0; i < sizeInBytes; i++) {
            this.data[i] = new Word(this.registerSize);
        }
    }

    public void clearMapping() {
        this.setMapping(new Mapping());
    }
    
    public String[] getSectorsNames(){
        String[] ret=new String[this.mapping.sectors.size()];
        for(int i=0; i<ret.length; i++){
            ret[i]=this.mapping.sectors.get(i).name;
        }
        return ret;
    }
    
    public ArrayList<MemoryAdress> getAdressesFrom(int quantity, int start){
        ArrayList<MemoryAdress> ret=new ArrayList<>();
        for(int i=0; i<quantity; i++){
            MemoryAdress aux=getAdress(start+i);
            if(aux==null){
                return ret;
            }
            ret.add(getAdress(start+i));
        }
        return ret;
    }
    
    public MemoryAdress getAdress(int index){
        if(index>=this.data.length || index<0){
            return null;
        }
        String sector=this.belongsTo(index);
        Word value=this.getWord(index);
        return new MemoryAdress(sector, index, value);
    }
    
    public class MemoryAdress{
        public String sector="";
        public int address;
        public Word value=new Word(0);

        public MemoryAdress(String sector, int address, Word value) {
            this.sector = sector;
            this.address = address;
            this.value = value;
        }   
    }
}
