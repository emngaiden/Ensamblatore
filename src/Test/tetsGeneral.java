/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Core.Architecture.Architecture;
import Core.Architecture.Memory.Memory;
import Core.Architecture.Word;
import Core.Assembler.AddressingMode;
import Core.Assembler.Assembler;
import Core.Assembler.Instruction.Directive;
import Core.Assembler.Instruction.Instruction;
import Core.Assembler.Instruction.InstructionFormat;
import Core.Assembler.Instruction.InstructionSector;
import Core.Assembler.Instruction.InstructionType;
import Core.Assembler.Instruction.Line;
import Core.Assembler.Instruction.SectionValue;
import Core.CPU;
import FileManager.Serializer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author emnga
 */
public class tetsGeneral {
    public static void main(String[] args) {
        int wordSize=32;
        CPU cpu=new CPU("RichieCore i99");
        Memory[] memories=new Memory[1];
        memories[0]=new Memory(30,8,Architecture.DATAMEMORYNAME);
        Architecture architecture=new Architecture(memories, 1, cpu);
        architecture.setProcesorBits(8);
        architecture.setWordSize(32);
        architecture.setEndianism(Architecture.Endianess.BIG);
        architecture.setProgramInitialRegister(20);
        architecture.setDataInitialRegister(0);
        cpu.setArchitecture(architecture);
        Assembler assembler=new Assembler(cpu);        
        assembler.printSuperSintaxes=false;
        cpu.setAssembler(assembler);
        
        ArrayList<InstructionSector> sectors1=new ArrayList<>();
        sectors1.add(new InstructionSector(0,5,"OPCODE",AddressingMode.INMEDIATO));
        sectors1.add(new InstructionSector(6,10,"rs",AddressingMode.INMEDIATO));
        sectors1.add(new InstructionSector(11,15,"rt",AddressingMode.INMEDIATO));
        sectors1.add(new InstructionSector(16,20,"rd",AddressingMode.INMEDIATO));
        sectors1.add(new InstructionSector(21,25,"shamt",AddressingMode.INMEDIATO));
        sectors1.add(new InstructionSector(26,31,"funct",AddressingMode.INMEDIATO));
        InstructionFormat format1=new  InstructionFormat(wordSize, sectors1);
        InstructionType t1=new InstructionType(format1, "R");
        assembler.addInstructionType(t1);
        
        //TIPO I
        ArrayList<InstructionSector> sectors2=new ArrayList<>();
        sectors2.add(new InstructionSector(0,5,"OPCODE",AddressingMode.INMEDIATO));
        sectors2.add(new InstructionSector(6,10,"rs",AddressingMode.INMEDIATO));
        sectors2.add(new InstructionSector(11,15,"rt",AddressingMode.INMEDIATO));
        sectors2.add(new InstructionSector(16,31,"inmediate",AddressingMode.INMEDIATO));
        InstructionFormat format2=new  InstructionFormat(wordSize, sectors2);
        InstructionType t2=new InstructionType(format2, "I");
        assembler.addInstructionType(t2);
        
        //TIPO J
        ArrayList<InstructionSector> sectors3=new ArrayList<>();
        sectors3.add(new InstructionSector(0,5,"OPCODE",AddressingMode.INMEDIATO));
        sectors3.add(new InstructionSector(6,31,"address",AddressingMode.INMEDIATO));
        InstructionFormat format3=new  InstructionFormat(wordSize, sectors3);
        InstructionType t3=new InstructionType(format3, "J");
        assembler.addInstructionType(t3);
        
        //Instrucción add, ie: add d20, $ads
        Instruction add=new Instruction(t1,"ADD" , assembler,new SectionValue[]{
            new SectionValue("opcode", new Word(6,0,0,0,0,0,0)),
            new SectionValue("funct", new Word(6,1,0,0,0,0,0)),
            new SectionValue("shamt", new Word(5,0,0,0,0,0)),
        });
        add.setSintax("add <VAR1|VALUE>, <VAR1|VALUE>, <VAR1>");
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
        lw.dataOrder= new String[]{"rs","inmediate","rt"};
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
        var1.setAsVariableUsage();
        assembler.addLine(var1);
        
        Line var2=new Line("VAR2");
        var2.setSintax("<INTEGER>\\(<VAR1>\\)");
        var2.setAsVariableUsage();
        assembler.addLine(var2);
        
        Line.init(assembler);
        Directive.init(assembler);
        String program=".data 15\n"+
                    ".var registerx = 0xa;\n" +
                    ".var registery = 0b1100;\n" +
                    "\n" +
                    "add $registerx, 4, $registery;\n"+
                    "lw $registerx, 255($registery)";
        System.out.println("Program: \n"+program);
        System.out.println("");
        Word[] result=cpu.getAssembler().runProgram(program);
        System.out.println(cpu.getAssembler().getListingFile().toString());
        System.out.println(cpu.getArchitecture().memoriesToString());
        System.out.println(Arrays.deepToString(result));
        FileManager.Serializer ser=new Serializer();
        ser.writeObjectFile("real.encpu", cpu);
    }
}
