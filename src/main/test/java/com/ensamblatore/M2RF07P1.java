/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ensamblatore;

import com.ensamblatore.core.architecture.Architecture;
import com.ensamblatore.core.architecture.memory.Memory;
import com.ensamblatore.core.architecture.Word;
import com.ensamblatore.core.assembler.Assembler;
import com.ensamblatore.core.assembler.Instruction.Directive;
import com.ensamblatore.core.assembler.Instruction.Instruction;
import com.ensamblatore.core.assembler.Instruction.InstructionFormat;
import com.ensamblatore.core.assembler.Instruction.InstructionSector;
import com.ensamblatore.core.assembler.Instruction.InstructionType;
import com.ensamblatore.core.assembler.Instruction.Line;
import com.ensamblatore.core.assembler.Instruction.SectionValue;
import com.ensamblatore.core.CPU;
import java.util.ArrayList;

/**
 *
 * @author emnga
 */
public class M2RF07P1 {
    public static void main(String[] args) {
        long timeStart=System.currentTimeMillis();
        //crea CPU
        CPU cp=new CPU("PICmicro DS31029A");
        cp.setVersionCode(1);
        cp.setVersionDescription("1.0");
        
        //Crea arquitectura y datos de arquitectura
        int wordSize1=32;
        int PCIncrement=1;
        Memory[] memory=new Memory[2];
        memory[0]=new Memory(30, 8, Architecture.DATAMEMORYNAME, null);
        memory[1]=new Memory(30, 8, Architecture.PROGRAMMEMORYNAME, null);
        Architecture architecture=new Architecture(memory, PCIncrement, cp);
        architecture.setEndianism(Architecture.Endianess.BIG);
        cp.setArchitecture(architecture);
        
        //Crea ensamblador y datos
        Assembler assembler=new Assembler(cp);
        assembler.printSuperSintaxes=true;
        cp.setAssembler(assembler);
        
        //Crea tipo de instrucciones
        ArrayList<InstructionSector> sectors1=new ArrayList<>();
        sectors1.add(new InstructionSector(0,5,"OPCODE"));
        sectors1.add(new InstructionSector(6,10,"rs"));
        sectors1.add(new InstructionSector(11,15,"rt"));
        sectors1.add(new InstructionSector(16,20,"rd"));
        sectors1.add(new InstructionSector(21,25,"shamt"));
        sectors1.add(new InstructionSector(26,31,"funct"));
        InstructionFormat format1=new  InstructionFormat(wordSize1, sectors1);
        InstructionType t1=new InstructionType(format1, "R");
        assembler.addInstructionType(t1);
        
        //TIPO I
        ArrayList<InstructionSector> sectors2=new ArrayList<>();
        sectors2.add(new InstructionSector(0,5,"OPCODE"));
        sectors2.add(new InstructionSector(6,10,"rs"));
        sectors2.add(new InstructionSector(11,15,"rt"));
        sectors2.add(new InstructionSector(16,31,"inmediate"));
        InstructionFormat format2=new  InstructionFormat(wordSize1, sectors2);
        InstructionType t2=new InstructionType(format2, "I");
        assembler.addInstructionType(t2);
        
        //TIPO J
        ArrayList<InstructionSector> sectors3=new ArrayList<>();
        sectors3.add(new InstructionSector(0,5,"OPCODE"));
        sectors3.add(new InstructionSector(6,31,"address"));
        InstructionFormat format3=new  InstructionFormat(wordSize1, sectors3);
        InstructionType t3=new InstructionType(format3, "J");
        assembler.addInstructionType(t3);
        
        //Instrucción add, ie: add d20, $ads
        Instruction add=new Instruction(t1,"ADD" , assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("funct", new Word(6,1,0,0,0,0,0)),
            new SectionValue("shamt", new Word(5,0,0,0,0,0)),
        });
        add.setSintax("add <VAR1>, <VAR1>, <VAR1>");
        add.createFormat();
        assembler.addInstruction(add);
        
        Instruction sub=new Instruction(t1,"SUB",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("shamt", new Word(5,0,0,0,0,0)),
            new SectionValue("funct",new Word(6,1,0,0,0,1,0))
        });
        sub.setSintax("sub <VAR1>, <VAR1>, <VAR1>");
        sub.createFormat();
        assembler.addInstruction(sub);
        
        Instruction and=new Instruction(t1,"AND",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("shamt", new Word(5,0,0,0,0,0)),
            new SectionValue("funct",new Word(6,1,0,0,1,0,0))
        });
        and.setSintax("and <VAR1>, <VAR1>, <VAR1>");
        and.createFormat();
        assembler.addInstruction(and);
        
        Instruction or=new Instruction(t1,"OR",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("shamt", new Word(5,0,0,0,0,0)),
            new SectionValue("funct",new Word(6,1,0,0,1,0,1))
        });
        or.setSintax("or <VAR1>, <VAR1>, <VAR1>");
        or.createFormat();
        assembler.addInstruction(or);
        
        Instruction lw=new Instruction(t2,"LW",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,1,0,0,0,1,1)),
        });
        lw.setSintax("lw <VAR1>, <VAR2>");
        lw.createFormat();
        assembler.addInstruction(lw);
        
        Instruction sll=new Instruction(t1,"SLL",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("rs", new Word(5,0,0,0,0,0)),
            new SectionValue("funct",new Word(6,0,0,0,0,0,0))
        });
        sll.setSintax("sll <VAR1>, <VAR1>, <VAR1>");
        sll.createFormat();
        assembler.addInstruction(sll);
        
        Instruction beq=new Instruction(t2,"BEQ",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,1,0,0)),
        });
        beq.setSintax("beq <VAR1>, <VAR1>, <INTEGER>");
        beq.createFormat();
        assembler.addInstruction(beq);
        
        Instruction j=new Instruction(t3,"J",assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,1,0)),
        });
        j.setSintax("j <INTEGER>");
        j.createFormat();
        assembler.addInstruction(j);
        
        Line var1=new Line("VAR1");
        var1.setSintax("\\$<IDENTIFIER>");
        assembler.addLine(var1);
        
        Line var2=new Line("VAR2");
        var2.setSintax("<INTEGER>\\(<VAR1>\\)");
        assembler.addLine(var2);
        Line.init(assembler);
        Directive.init(assembler);
        cp.getAssembler().runProgram("xorlw 10 ;hello world!!!!!");
        System.out.println(System.currentTimeMillis()-timeStart);
    }
    
    public static SectionValue[] createSectionValues(String[] names, Word[] data){
        SectionValue[] ret=new SectionValue[names.length];
        for(int i=0; i<names.length; i++){
            ret[i]=new SectionValue(names[i], data[i]);
        }
        return ret;
    }
    
    public static String[] createStringArray(String... data){
        return data;
    }
    
    public static Word createWord(int size,int... data){
        return new Word(size,data);
    }
    
    public static Word[] createWordArray(Word... data){
        return data;
    }
    public static void createInstruction(InstructionType type,String identifier,Assembler parent, SectionValue[] values,String sintax,String[] order){
        Instruction ret=new Instruction(type,identifier, parent, values);
        ret.setSintax(sintax);
        ret.createFormat();
        ret.dataOrder=order;
        ret.addToParent();
    }
}
