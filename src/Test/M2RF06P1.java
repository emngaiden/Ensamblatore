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
package Test;

import Core.Architecture.Word;
import Core.Assembler.AddressingMode;
import Core.Assembler.Assembler;
import Core.Assembler.Instruction.Instruction;
import Core.Assembler.Instruction.InstructionFormat;
import Core.Assembler.Instruction.InstructionSector;
import Core.Assembler.Instruction.InstructionType;
import Core.Assembler.Instruction.Line;
import Core.Assembler.Instruction.SectionValue;
import java.util.ArrayList;

/**
 *
 * @author emnga
 */
public class M2RF06P1 {
    public static void main(String[] args) {
        Assembler ass1=new Assembler();
        ass1.printSuperSintaxes=false;
        int wordSize1=32;
        /*
        *Tipo R: Identifier= R
        *Tipo I: Identifier= I
        *Tipo J: Identifier= J
        */
        //TIPO R
        ArrayList<InstructionSector> sectors1=new ArrayList<>();
        sectors1.add(new InstructionSector(0,5,"OPCODE",AddressingMode.INMEDIATO));
        sectors1.add(new InstructionSector(6,10,"rs",AddressingMode.INMEDIATO));
        sectors1.add(new InstructionSector(11,15,"rt",AddressingMode.INMEDIATO));
        sectors1.add(new InstructionSector(16,20,"rd",AddressingMode.INMEDIATO));
        sectors1.add(new InstructionSector(21,25,"shamt",AddressingMode.INMEDIATO));
        sectors1.add(new InstructionSector(26,31,"funct",AddressingMode.INMEDIATO));
        InstructionFormat format1=new  InstructionFormat(wordSize1, sectors1);
        InstructionType t1=new InstructionType(format1, "R");
        ass1.addInstructionType(t1);
        
        //TIPO I
        ArrayList<InstructionSector> sectors2=new ArrayList<>();
        sectors2.add(new InstructionSector(0,5,"OPCODE",AddressingMode.INMEDIATO));
        sectors2.add(new InstructionSector(6,10,"rs",AddressingMode.INMEDIATO));
        sectors2.add(new InstructionSector(11,15,"rt",AddressingMode.INMEDIATO));
        sectors2.add(new InstructionSector(16,31,"inmediate",AddressingMode.INMEDIATO));
        InstructionFormat format2=new  InstructionFormat(wordSize1, sectors2);
        InstructionType t2=new InstructionType(format2, "I");
        ass1.addInstructionType(t2);
        
        //TIPO J
        ArrayList<InstructionSector> sectors3=new ArrayList<>();
        sectors3.add(new InstructionSector(0,5,"OPCODE",AddressingMode.INMEDIATO));
        sectors3.add(new InstructionSector(6,31,"address",AddressingMode.INMEDIATO));
        InstructionFormat format3=new  InstructionFormat(wordSize1, sectors3);
        InstructionType t3=new InstructionType(format3, "J");
        ass1.addInstructionType(t3);
        
        //Instrucción add, ie: add d20, $ads
        Instruction add=new Instruction(t1,"ADD" , ass1,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("funct", new Word(6,1,0,0,0,0,0)),
            new SectionValue("shamt", new Word(5,0,0,0,0,0)),
        });
        add.setSintax("add <VAR1>, <VAR1>, <VAR1>");
        add.createFormat();
        ass1.addInstruction(add);
        
        Instruction sub=new Instruction(t1,"SUB",ass1,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("shamt", new Word(5,0,0,0,0,0)),
            new SectionValue("funct",new Word(6,1,0,0,0,1,0))
        });
        sub.setSintax("sub <VAR1>, <VAR1>, <VAR1>");
        sub.createFormat();
        ass1.addInstruction(sub);
        
        Instruction and=new Instruction(t1,"AND",ass1,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("shamt", new Word(5,0,0,0,0,0)),
            new SectionValue("funct",new Word(6,1,0,0,1,0,0))
        });
        and.setSintax("and <VAR1>, <VAR1>, <VAR1>");
        and.createFormat();
        ass1.addInstruction(and);
        
        Instruction or=new Instruction(t1,"OR",ass1,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("shamt", new Word(5,0,0,0,0,0)),
            new SectionValue("funct",new Word(6,1,0,0,1,0,1))
        });
        or.setSintax("or <VAR1>, <VAR1>, <VAR1>");
        or.createFormat();
        ass1.addInstruction(or);
        
        Instruction lw=new Instruction(t2,"LW",ass1,new SectionValue[]{
            new SectionValue("opcode", new Word(6,1,0,0,0,1,1)),
        });
        lw.setSintax("lw <VAR1>, <VAR2>");
        lw.createFormat();
        ass1.addInstruction(lw);
        
        Instruction sll=new Instruction(t1,"SLL",ass1,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("rs", new Word(5,0,0,0,0,0)),
            new SectionValue("funct",new Word(6,0,0,0,0,0,0))
        });
        sll.setSintax("sll <VAR1>, <VAR1>, <VAR1>");
        sll.createFormat();
        ass1.addInstruction(sll);
        
        Instruction beq=new Instruction(t2,"BEQ",ass1,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,1,0,0)),
        });
        beq.setSintax("beq <VAR1>, <VAR1>, <INTEGER>");
        beq.createFormat();
        ass1.addInstruction(beq);
        
        Instruction j=new Instruction(t3,"J",ass1,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,1,0)),
        });
        j.setSintax("j <INTEGER>");
        j.createFormat();
        ass1.addInstruction(j);
        
        Line var1=new Line("VAR1");
        var1.setSintax("\\$<IDENTIFIER>");
        ass1.addLine(var1);
        
        Line var2=new Line("VAR2");
        var2.setSintax("<INTEGER>\\(<VAR1>\\)");
        ass1.addLine(var2);
        
        Line.init(ass1);
        
        String toVerify="beq $sd, $pp, 4";
        System.out.println(toVerify);
        System.out.println(ass1.getInstructionSintaxIndex(toVerify));
        ass1.modifyInstructionSintaxByIdentifier("VAR1", "#<IDENTIFIER>");
        toVerify="beq $sd, $pp, 0xaf";
        
        System.out.println(toVerify);
        System.out.println(ass1.getInstructionSintaxIndex(toVerify));
        
        
    }
}
