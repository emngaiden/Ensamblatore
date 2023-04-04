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
package com.ensamblatore.core.assembler;

import com.ensamblatore.core.architecture.Architecture;
import com.ensamblatore.core.architecture.memory.Memory;
import com.ensamblatore.core.architecture.Word;
import com.ensamblatore.core.assembler.format.Sintax;
import com.ensamblatore.core.assembler.instruction.Directive;
import com.ensamblatore.core.assembler.instruction.FormatBuilder;
import com.ensamblatore.core.assembler.instruction.Instruction;
import com.ensamblatore.core.assembler.instruction.InstructionType;
import com.ensamblatore.core.assembler.instruction.Line;
import com.ensamblatore.core.assembler.instruction.Value;
import com.ensamblatore.core.assembler.ListingFile.Variable;
import com.ensamblatore.core.CPU;
import com.ensamblatore.exceptions.AssemblyException;
import com.ensamblatore.exceptions.ParsingException;
import com.ensamblatore.exceptions.ReferenceNotFoundException;
import com.ensamblatore.exceptions.SintaxException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author emnga
 */
public class Assembler implements Serializable {

    private CPU parent;
    private ArrayList<InstructionType> instructionTypes;
    private ArrayList<Line> instructions;
    private ArrayList<Instruction> _instructions;
    private ArrayList<Line> _lines;
    private ArrayList<Directive> _directives;
    private ArrayList<Line> _varusages;
    private ArrayList<Line> _registerusage;
    private ArrayList<Line> _memoryusage;
    private boolean isSorted = false;
    public boolean printSuperSintaxes = false;
    public boolean canHaveEmptySectors = false;
    public int[] memoriesWritePointers;
    private ListingFile variablesTable;

    public Assembler() {
        this._directives = new ArrayList<>();
        this._lines = new ArrayList<>();
        this._instructions = new ArrayList<>();
        this._varusages = new ArrayList<>();
        this._registerusage=new ArrayList<>();
        this._memoryusage=new ArrayList<>();
        this.instructionTypes = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    public void repoblateInstructionsFrom_Lists() {
        Directive.DEFAULT_DIRECTIVES = this._directives;
        this.instructions = new ArrayList<>();
        this.instructions.addAll(_instructions);
        this.instructions.addAll(_lines);
        this.instructions.addAll(_varusages);
        this.instructions.addAll(_directives);
        this.instructions.addAll(_registerusage);
        this.instructions.addAll(_memoryusage);
    }

    public Assembler(CPU parent) {
        this();
        this.parent = parent;
    }

    public ArrayList<Directive> get_Directives() {
        return this._directives;
    }

    public ArrayList<Line> get_Lines() {
        return this._lines;
    }

    public ArrayList<Instruction> get_Instructions() {
        return this._instructions;
    }

    public ArrayList<Line> get_Varusages() {
        return this._varusages;
    }

    public ArrayList<Line> getRegisterusage() {
        return _registerusage;
    }

    public ArrayList<Line> getMemoryusage() {
        return _memoryusage;
    }

    public boolean modifyInstructionSintaxByIdentifier(String identifier, String toReplace) {
        Line line = this.getInstructionFromIdentifier(identifier);
        if (line == null) {
            return false;
        }
        line.setSintax(toReplace);
        deleteSuperSintax(line);
        return true;
    }

    public Line getInstructionFromIdentifier(String identifier) {
        for (Line line : this.instructions) {
            if (line.getIdentifier().equals(identifier)) {
                return line;
            }
        }
        return null;
    }

    public Sintax getSintaxFromInstructionIdentifier(String identifier) {
        for (Line inst : this.instructions) {
            if (inst.getIdentifier().equals(identifier)) {
                return inst.getSintax();
            }
        }
        return null;
    }

    public Sintax getSintaxFromInstructionIndex(int instructionIndex) {
        return this.instructions.get(instructionIndex).getSintax();
    }

    public boolean isStringValidForLine(Line line, String toCheck) {
        if (line.superSintax == null) {
            line.superSintax = this.getSuperSintax(line);
        }
        if (toCheck.matches(line.superSintax)) {
            return true;
        }
        return false;
    }

    public int getInstructionSintaxIndex(String stringToVerifyOriginal) {
        for (int i = 0; i < this.instructions.size(); i++) {
            Line found = this.instructions.get(i);
            if (found.superSintax == null) {
                found.superSintax = this.getSuperSintax(found);
            }
            String superaux =  found.superSintax;
            if (stringToVerifyOriginal.matches(superaux)) {
                return i;
            }
        }
        return -1;
    }

    public ArrayList<String> getArguments(String toExtract, Line lineOfString) {
        String sintax = lineOfString.superSintax;
        ArrayList<String> arguments = new ArrayList<>();
        String[] extracted = Line.extractTextInParenthesis(sintax, true);
        StringBuilder sb = new StringBuilder();
        int number = 0;
        ArrayList<String> patterns = new ArrayList<>();
        for (String aux : extracted) {
            aux = aux.substring(1, aux.length() - 1);
            number += 1;
            if (!"\\s".equals(aux) && !"\\.".equals(aux)) {
                sb.append('$').append(number);
                patterns.add(sb.toString());
                sb = new StringBuilder();
                number += (Line.countParenthesis(aux, false));
            }
        }
        for (String pat : patterns) {
            String fixed = toExtract.replaceAll(sintax, pat);
            arguments.add(fixed);
        }
        return arguments;
    }

    public String recursiveSintax(String sintax) {
        if (sintax.contains(Line.identifierOpenerCharacter + "")) {
            String identifiers;
            String merge1, merge2;
            try {
                int from = 0, to = 0;
                for (int i = 0; i < sintax.length(); i++) {
                    char found = sintax.charAt(i);
                    if (found == Line.identifierOpenerCharacter) {
                        from = i + 1;
                    } else if (found == Line.identifierCloserCharacter) {
                        to = i;
                        break;
                    }
                }
                identifiers = sintax.substring(from, to);
                merge1 = sintax.substring(0, from - 1);
                merge2 = sintax.substring(to + 1, sintax.length());
            } catch (StringIndexOutOfBoundsException e) {
                throw new ParsingException("\""
                        + Line.identifierCloserCharacter + "\" or \"" + Line.identifierCloserCharacter + "\" not found");
            }
            if (!identifiers.contains(Line.identifierDividerCharacter + "")) {
                try {
                    sintax = merge1 + "(" + this.getSintaxFromInstructionIdentifier(identifiers).getRegex() + ")" + merge2;
                } catch (NullPointerException e) {
                    throw new ReferenceNotFoundException("Instructions", identifiers);
                }
            } else {
                String[] ident;
                boolean aux = false;
                for (char scappable : Sintax.SPECIALCHARACTERS) {
                    if (Line.identifierDividerCharacter == scappable) {
                        aux = true;
                        break;
                    }
                }
                if (!aux) {
                    ident = identifiers.split(Line.identifierDividerCharacter + "");
                } else {
                    ident = identifiers.split("\\" + Line.identifierDividerCharacter);
                }
                String[] sintaxes = new String[ident.length];
                String replacement = "(";
                for (int i = 0; i < ident.length; i++) {
                    sintaxes[i] = this.getSintaxFromInstructionIdentifier(ident[i]).getRegex();
                }
                for (int i = 0; i < sintaxes.length - 1; i++) {
                    replacement += "(";
                    replacement += sintaxes[i];
                    replacement += ")|";
                }
                replacement += "(";
                replacement += sintaxes[sintaxes.length - 1];
                replacement += "))";
                sintax = merge1 + replacement + merge2;
            }
            if (sintax.contains(Line.identifierOpenerCharacter + "")) {
                return recursiveSintax(sintax);
            }
        }
        return sintax;
    }

    public String getSuperSintax(Sintax sintax) {
        String original = sintax.getRegex();
        return recursiveSintax(original);
    }

    public String getSuperSintax(int id) {
        return getSuperSintax(this.getSintaxFromInstructionIndex(id));
    }

    public String getSuperSintax(String identifier) {
        return getSuperSintax(this.getSintaxFromInstructionIdentifier(identifier));
    }

    public String getSuperSintax(Line inst) {
        String aux = getSuperSintax(this.getSintaxFromInstructionIdentifier(inst.identifier));
        //TODO: Fix this
        //return Sintax.fixCharacterScapes(aux);
        if (printSuperSintaxes) {
            System.out.println(aux);
        }
        return aux;
    }

    public boolean addInstruction(Instruction instruction) {
        if (!this.instructions.stream().noneMatch((inst) -> (inst.getIdentifier().equals(instruction.getIdentifier())))) {
            return false;
        }
        this.instructions.add(instruction);
        this._instructions.add(instruction);
        this.isSorted = false;
        return true;
    }

    public boolean addInstructions(ArrayList<Instruction> instructions) {
        boolean error = false;
        for (Instruction line : instructions) {
            if (!this.addInstruction(line) && error) {
                error = true;
            }
        }
        this.isSorted = false;
        return true;
    }

    public boolean addDefaultDirective(Directive dir) {
        for (Line line : this.getInstructions()) {
            if (line.getIdentifier().equals(dir.identifier)) {
                return false;
            }
        }
        this.instructions.add(dir);
        this.isSorted = false;
        this._directives.add(dir);
        return true;
    }

    public boolean addLine(Line instruction) {
        if (!this.instructions.stream().noneMatch((inst) -> (inst.getIdentifier().equals(instruction.getIdentifier())))) {
            return false;
        }
        this.instructions.add(instruction);
        switch(instruction.id){
            case Line.ID_LINE:
                this._lines.add(instruction);
                break;
            case Line.ID_VARIABLEUSAGE:
                this._varusages.add(instruction);
                break;
            case Line.ID_MEMORY:
                this._memoryusage.add(instruction);
                break;
            case Line.ID_REGISTE:
                this._registerusage.add(instruction);
                break;
        }
        this.isSorted = false;
        return true;
    }

    public boolean addLines(ArrayList<Line> instructions) {
        boolean error = false;
        for (Line line : instructions) {
            if (!this.addLine(line) && error) {
                error = true;
            }
        }
        this.isSorted = false;
        return true;

    }

    public boolean addInstructionType(InstructionType instruction) {
        if (!this.instructionTypes.stream().noneMatch((inst) -> (inst.getIdentifier().equals(instruction.getIdentifier())))) {
            return false;
        }
        this.instructionTypes.add(instruction);
        return true;
    }

    public CPU getParent() {
        return parent;
    }

    public ArrayList<InstructionType> getInstructionTypes() {
        return instructionTypes;
    }

    public Line getLine(int index) {
        return this.instructions.get(index);
    }

    public boolean isSorted() {
        return this.isSorted;
    }

    public void sortInstructions() {
        if (!this.isSorted) {
            this.instructions.sort(new InstrComparator());
            this.isSorted = true;
        }
    }

    public Word[] runProgram(String program) {
        //program=Sintax.scapeAllParenthesis(program);
        Line.init(this);
        Directive.init(this);
        this.getParent().getArchitecture().sortMemories();
        Line registerNames = new Line("REGISTERNAME", buildRegisterNamesSintax(this.parent.getArchitecture().getMemoryFromIdentifier(Architecture.DATAMEMORYNAME).getSectorsNames()));
        this.instructions.add(registerNames);
        this.sortInstructions();
        Word[] ret;
        this.variablesTable = new ListingFile();
        ArrayList<String> lines = null;
        lines = ignoreComments(program);
        String errors = verifySintaxes(lines);
        if (errors.contains("Error")) {
            throw new SintaxException(errors);
        }
        String[] results = errors.split(",");
        int[] id = new int[results.length];
        for (int i = 0; i < results.length; i++) {
            id[i] = Integer.parseInt(results[i]);
        }
        errors = runDirectives(lines, id);
        if (!errors.isEmpty()) {
            throw new AssemblyException(errors);
        }
        ret = solveVariables(lines, id);
        return ret;
    }

    private void deleteSuperSintax(Line identifier) {
        identifier.superSintax = null;
        for (Line lin : this.instructions) {
            if (lin.sintax.getRegex().contains(identifier.getIdentifier())) {
                deleteSuperSintax(lin);
            }
        }
    }

    public ListingFile getListingFile() {
        return variablesTable;
    }

    private ArrayList<String> ignoreComments(String bb) {
        int openerCounter = bb.indexOf(Line.longCommentOpener);
        int closerCounter;
        while (openerCounter != -1) {
            closerCounter = bb.indexOf(Line.longCommentCloser, openerCounter + Line.longCommentCloser.length());
            if (closerCounter != -1) {
                String aux = bb.substring(0, openerCounter);
                String aux2 = bb.substring(closerCounter + Line.longCommentCloser.length(), bb.length());
                bb = aux + aux2;
            } else {
                throw new ParsingException("Missing comment closer sequence");
            }
            openerCounter = bb.indexOf(Line.longCommentOpener);
        }
        String[] liness = bb.split("\n");
        ArrayList<String> lines = new ArrayList<>();
        for (String line : liness) {
            if (!line.isEmpty()) {
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == Line.inLineCommentCharacter) {
                        line = line.substring(0, i);
                        break;
                    }
                }
                lines.add(line);
            }
        }
        return lines;
    }

    private String verifySintaxes(ArrayList<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int id = this.getInstructionSintaxIndex(line);
            if (id == -1) {
                sb = new StringBuilder();
                sb.append("Error,").append(i + 1).append(",").append(line);
                break;
            } else {
                sb.append(id).append(',');
            }
        }
        return sb.toString();
    }

    private String runDirectives(ArrayList<String> lines, int[] ids) {
        StringBuilder sb = new StringBuilder();
        ArrayList<Directive> directives = new ArrayList<>();
        ArrayList<String> newLines = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            Line got = this.instructions.get(ids[i]);
            if (got.id == Line.ID_DIRECTIVE) {
                directives.add((Directive) got);
                newLines.add(lines.get(i));
            }
        }
        if (!directives.isEmpty()) {
            directives.sort(new DirectiveComparator());
        }
        for (int i = 0; i < directives.size(); i++) {
            Directive dr = directives.get(i);
            try {
                dr.execute(newLines.get(i));
            } catch (ParsingException e) {
                sb.append(e.getMessage()).append('\n');
            }
        }
        return sb.toString();
    }

    public InstructionType getInstructionTypeFromIdentifier(String identifier) {
        for (InstructionType type : this.instructionTypes) {
            if (type.getIdentifier().equals(identifier)) {
                return type;
            }
        }
        return null;
    }

    public ArrayList<Line> getInstructions() {
        return instructions;
    }

    public void setInstructionTypes(ArrayList<InstructionType> instructionTypes) {
        this.instructionTypes = instructionTypes;
    }

    private Word[] solveVariables(ArrayList<String> lines, int[] id) {
        ArrayList<Instruction> instructs = new ArrayList<>();
        ArrayList<String> newLines = new ArrayList<>();
        ArrayList<Line> varusages = getVariableUsages();
        ArrayList<Line> registerusage= getRegisterusage();
        ArrayList<Line> memoryusage = getMemoryusage();
        for (int i = 0; i < id.length; i++) {
            Line got = this.instructions.get(id[i]);
            if (got.id == Line.ID_INSTRUCTION) {
                instructs.add((Instruction) got);
                newLines.add(lines.get(i));
            }
        }
        Word[] ret = new Word[instructs.size()];
        ArrayList<String> arguments;
        for (int i = 0; i < instructs.size(); i++) {
            Instruction ins = instructs.get(i);
            String line = newLines.get(i);
            arguments = this.getArguments(line, ins);
            boolean infered = false;
            String[] dataOrder = instructs.get(i).dataOrder;
            if (dataOrder == null) {
                dataOrder = ins.getType().getFormat().getSectionsNames();
                infered = true;
            }
            FormatBuilder builder = new FormatBuilder(ins, dataOrder, infered);
            ArrayList<ArgumentValue> argumentValues = new ArrayList<>();
            for (int j = 0; j < arguments.size(); j++) {
                boolean added = false;
                boolean isValid = false;
                String arg = arguments.get(j);
                int defaultLineIdent = Line.identifyLine(arg,this);
                if (defaultLineIdent == -1) {
                    for (Line usage : varusages) {
                        if (this.isStringValidForLine(usage, arg)) {
                            arguments.remove(j);
                            arguments.addAll(j, this.getArguments(arg, usage));
                            j--;
                            added = true;
                            break;
                        }
                    }
                    /*for(Line usage: registerusage){
                        if (this.isStringValidForLine(usage, arg)) {
                            arguments.remove(j);
                            arguments.addAll(j, this.getArguments(arg, usage));
                            j--;
                            added = true;
                            break;
                        }
                    }
                    for(Line usage: memoryusage){
                        if (this.isStringValidForLine(usage, arg)) {
                            arguments.remove(j);
                            arguments.addAll(j, this.getArguments(arg, usage));
                            j--;
                            added = true;
                            break;
                        }
                    }
                    */
                } else {
                    isValid = true;
                    argumentValues.add(new ArgumentValue(arg, defaultLineIdent));
                }
                if (!added) {
                    if (!isValid) {
                        throw new AssemblyException("Invalid argument " + arg + " in " + line);
                    }
                }
            }
            for (ArgumentValue v : argumentValues) {
                Variable value=null;
                if (v.type == 1 || v.type == 6) {
                    value = this.variablesTable.findVariable(v.arg);
                    Word toInsert=Word.parseWord(Value.getIntegerValueInstance(value.getPositionInMemory()).toBinary().getRawValue());
                    toInsert.trimToSize();
                    builder.insertValue(toInsert);
                }else if(v.type==7){
                    ArrayList<String> args=this.getArguments(v.arg, Line.getLine("REGISTER"));
                    Memory aux=this.parent.getArchitecture().getMemoryFromIdentifier(args.get(0));
                    if(aux==null)
                        throw new ReferenceNotFoundException(args.get(0), v.arg);
                    Word toInsert=aux.getWord(Integer.parseInt(args.get(1)));
                    toInsert.trimToSize();
                    builder.insertValue(toInsert);
                }else if(v.type==8){
                    ArrayList<String> args=this.getArguments(v.arg, Line.getLine("DEFAULTREGISTER"));
                    Word toInsert=this.parent.getArchitecture().getMemoryFromIdentifier(Architecture.DATAMEMORYNAME).getWord(Integer.parseInt(args.get(0)));
                    toInsert.trimToSize();
                    builder.insertValue(toInsert);
                } 
                else {
                    Value val = new Value(v.arg, v.type, false);
                    Word toInsert = Word.parseWord(val.toBinary().getRawValue());
                    toInsert.trimToSize();
                    builder.insertValue(toInsert);
                }
                //Causes "Zero means Error" error
                //value.trimToSize();
                //builder.insertValue(value);
            }
            ret[i] = builder.createWord(this.canHaveEmptySectors);
            if (this.parent.getArchitecture().getMemoriesCount() > 1) {
                Memory data = this.getParent().getArchitecture().getMemoryFromIdentifier(Architecture.PROGRAMMEMORYNAME);
                if(this.memoriesWritePointers==null){
                    this.memoriesWritePointers=this.parent.getArchitecture().initMemoryCounters();
                }
                int writePointer = this.memoriesWritePointers[data.id];
                int idid = data.setWord(writePointer, ret[i]);
                this.memoriesWritePointers[data.id] += idid;
            } else {
                Memory data = this.getParent().getArchitecture().getMemory()[0];
                int writePointer = this.memoriesWritePointers[0];
                int idid = data.setWord(writePointer, ret[i]);
                this.memoriesWritePointers[0] += idid;
            }
        }
        return ret;
    }

    private ArrayList<Line> getVariableUsages() {
        ArrayList<Line> ret = new ArrayList<>();
        for (Line lin : this.instructions) {
            if (lin.id == Line.ID_VARIABLEUSAGE) {
                ret.add(lin);
            }
        }
        return ret;
    }

    private String buildRegisterNamesSintax(String[] sectorsNames) {
        if (sectorsNames.length == 0) {
            return Memory.DEFAULTREGISTERACCESNAME;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sectorsNames.length - 1; i++) {
            sb.append(sectorsNames[i]).append("|");
        }
        sb.append(sectorsNames[sectorsNames.length - 1]).append('|');
        sb.append(Memory.DEFAULTREGISTERACCESNAME);
        return sb.toString();
    }

    private class InstrComparator implements Comparator<Line> {

        @Override
        public int compare(Line t, Line t1) {
            if (t.id > t1.id) {
                return 1;
            } else if (t.id < t1.id) {
                return -1;
            } else {
                return 0;
            }
        }

    }

    private class DirectiveComparator implements Comparator<Directive> {

        @Override
        public int compare(Directive t, Directive t1) {
            if (t.getPriority() > t1.getPriority()) {
                return -1;
            } else if (t.getPriority() < t1.getPriority()) {
                return 1;
            } else {
                return 0;
            }
        }

    }

    private class ArgumentValue implements Serializable {

        public String arg;
        public int type;

        public ArgumentValue(String arg, int type) {
            this.arg = arg;
            this.type = type;
        }
    }

    public void set_Instructions(ArrayList<Instruction> _instructions) {
        this._instructions = _instructions;
    }

    public void set_Lines(ArrayList<Line> _lines) {
        this._lines = _lines;
    }

    public void set_Directives(ArrayList<Directive> _directives) {
        this._directives = _directives;
    }

    public void set_Varusages(ArrayList<Line> _varusages) {
        this._varusages = _varusages;
    }

    public void setRegisterusage(ArrayList<Line> _registerusage) {
        this._registerusage = _registerusage;
    }

    public void setMemoryusage(ArrayList<Line> _memoryusage) {
        this._memoryusage = _memoryusage;
    }
    
    

}
