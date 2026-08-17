package ca.uwaterloo.watform.alloyast.paragraph;

import static ca.uwaterloo.watform.parser.AlloyParser.*;
import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import antlr.generated.*;
import antlr.generated.DashBaseVisitor;
import antlr.generated.DashParser;
import ca.uwaterloo.watform.alloyast.*;
import ca.uwaterloo.watform.alloyast.expr.AlloyExprParseVis;
import ca.uwaterloo.watform.alloyast.expr.misc.*;
import ca.uwaterloo.watform.alloyast.expr.var.*;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdDeclParseVis;
import ca.uwaterloo.watform.alloyast.paragraph.command.AlloyCmdPara;
import ca.uwaterloo.watform.alloyast.paragraph.module.*;
import ca.uwaterloo.watform.alloyast.paragraph.module.AlloyModulePara.AlloyModuleArg;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigPara;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigQualParseVis;
import ca.uwaterloo.watform.alloyast.paragraph.sig.AlloySigRelParseVis;
import ca.uwaterloo.watform.utils.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.antlr.v4.runtime.tree.*;

public class AlloyParaParseVis extends DashBaseVisitor<AlloyPara> {
  protected final AlloyExprParseVis exprParseVis = new AlloyExprParseVis();
  protected final AlloySigRefsParseVis sigRefsParseVis = new AlloySigRefsParseVis();
  public final String fullFileName;
  public String moduleName = ""; // gets set when parse a ModulePara

  public AlloyParaParseVis(String fullFileName) {
    this.fullFileName = fullFileName;
  }

  @Override
  public AlloyPara visitParagraph(DashParser.ParagraphContext ctx) {
    return this.visit(ctx.getChild(0));
  }

  // ====================================================================================
  // Module
  // ====================================================================================
  @Override
  public AlloyModulePara visitModulePara(DashParser.ModuleParaContext ctx) {
    AlloyQnameExpr moduleNameExpr = (AlloyQnameExpr) exprParseVis.visit(ctx.qname());
    this.moduleName = moduleNameExpr.getName();
    return new AlloyModulePara(
        new Pos(ctx),
        moduleNameExpr,
        visitAll(ctx.moduleArg(), new AlloyModuleArgParseVis(), AlloyModuleArg.class));
  }

  private String computeImportFileName(
      String parentModuleName, String parentFileName, String importName) {

    // for non-util files
    // determine imported file name relative to the current path and parentModuleName
    // trying to match what AA does to create the file name that

    // Start with the directory containing parentFileName
    // strip off .als
    String parentFileNameTmp = parentFileName.substring(0, parentFileName.lastIndexOf('.'));

    if (parentFileNameTmp.endsWith(parentModuleName)) {
      String[] parentFileNameParts = parentFileNameTmp.split("/");
      String[] parentModuleParts = parentModuleName.split("/");
      int m = parentModuleParts.length - 1;
      int f = parentFileNameParts.length - 1;

      // look for common parts with parentModuleName and parentFileName
      while (m >= 0 && f >= 0 && parentModuleParts[m].equals(parentFileNameParts[f])) {
        // not sure about partial matches here
        m--;
        f--;
      }
      // chop off the common parts from the parentDir
      String parentDir;
      if (f == parentFileNameParts.length - 1) {
        parentDir = parentFileName.substring(0, parentFileName.lastIndexOf('/'));
      } else {
        parentDir = String.join("/", Arrays.copyOfRange(parentFileNameParts, 0, f + 1));
      }
      // add the importName
      String fileName = parentDir + "/" + importName + ".als";
      return fileName;

    } else {
      // parentModuleName does not end in XX/YY
      // parentFileName does not end in XX/YY

      // AA looks for how much of parentModuleName is matched with
      // importName and then creates file name from parentFileName
      // plus remainder after match minus 1
      // e.g.,
      // module aa
      // open aa/bb/cc  // looks for parentFileName/aa/bb/cc.als
      //
      // if there is no match, e.g.,
      // module XX
      // open aa/bb/cc // looks for parentFileName/aa/bb/cc.als
      //
      // another example:
      // module aa/bb/cc
      // open aa/bb/cc // looks for parentFilename/cc.als
      //
      // but the match between parentModuleName and importName
      // does not have to be full, as in:
      //
      // module aa/b
      // open aa/bb/cc/dd // looks for parentFileName/bb/cc/dd.als
      // this example was probably a mistake in the AA code
      // but we will live with it here to match AA behaviour

      String parentDir = parentFileName.substring(0, parentFileName.lastIndexOf('/'));

      String[] parentParts = parentModuleName.split("/");
      String[] importParts = importName.split("/");

      // Find longest common prefix.
      int common = 0;
      while (common < parentParts.length && common < importParts.length) {
        if (parentParts[common].equals(importParts[common])) {
          common++;
        } else if (importParts[common].startsWith(parentParts[common])) {
          // partial match - count as a match but don't continue
          // looking for matches
          common++;
          break;
        } else {
          // no match
          break;
        }
      }

      // Start with the directory containing parentFileName.
      // String parentDir = parentFileName.substring(0, parentFileName.lastIndexOf('/'));
      if (common == 1) {
        // System.out.println("here29");
        // System.out.println(parentDir + "/" + importName + ".als");
        return parentDir + "/" + importName + ".als";
      } else {

        // System.out.println("here30");
        // System.out.println(String.join("/", importParts));
        // System.out.println(common);
        // System.out.println(importParts.length);

        // if there is a partial match
        // go back by one match to get name
        String fileName =
            parentDir
                + "/"
                + String.join("/", Arrays.copyOfRange(importParts, common, importParts.length))
                + ".als";
        // System.out.println(fileName);

        return fileName;
      }
    }
  }

  // ====================================================================================
  // Import
  // ====================================================================================
  @Override
  public AlloyImportPara visitImportPara(DashParser.ImportParaContext ctx) {
    String importName = exprParseVis.visit(ctx.qname(0)).toString();
    AlloyFile importedAlloyFile;
    String parentModuleName = this.moduleName;
    String parentFileName = this.fullFileName;
    if (importName.startsWith("util/")) {
      // where the util files are stored in the jar
      // if not found, error will be issued via alloyParseUtilFile
      importedAlloyFile = alloyParseUtilFile(new Pos(ctx), importName);
    } else {
      // have to check if it exists on disk before checking jar
      String fullFileName = computeImportFileName(this.moduleName, this.fullFileName, importName);
      if (Files.exists(Paths.get(fullFileName))) {
        // file exists so parse the normal way
        importedAlloyFile = alloyParse(fullFileName);
      } else {
        // see if it is in the Alloy jar
        // System.out.println("import Name: " + importName);
        InputStream in =
            AlloyParaParseVis.class
                .getClassLoader()
                .getResourceAsStream("models/" + importName + ".als");
        if (in != null) {
          importedAlloyFile = alloyParseFromJar(new Pos(ctx), "models/" + importName + ".als");
        } else {
          // this will fail but will throw a standard error of file not found
          importedAlloyFile = alloyParse(fullFileName);
        }
      }
    }
    // next we have to put this alloyFile into some part of the import
    return new AlloyImportPara(
        new Pos(ctx),
        null != ctx.PRIVATE(),
        (AlloyQnameExpr) exprParseVis.visit(ctx.qname(0)),
        ((null != ctx.sigRefs())
            ? this.sigRefsParseVis.visit(ctx.sigRefs())
            : Collections.emptyList()),
        ((null != ctx.qname(1)) ? (AlloyQnameExpr) exprParseVis.visit(ctx.qname(1)) : null),
        importedAlloyFile);
  }

  // ====================================================================================
  // Sig
  // ====================================================================================
  @Override
  public AlloySigPara visitSigPara(DashParser.SigParaContext ctx) {
    return new AlloySigPara(
        new Pos(ctx),
        visitAll(ctx.sigQualifier(), new AlloySigQualParseVis(), AlloySigPara.Qual.class),
        null != ctx.qnames()
            ? visitAll(ctx.qnames().qname(), exprParseVis, AlloyQnameExpr.class)
            : Collections.emptyList(),
        null != ctx.sigRel() ? new AlloySigRelParseVis().visit(ctx.sigRel()) : null,
        visitAll(ctx.decl(), exprParseVis, AlloyDecl.class),
        null != ctx.block() ? (AlloyBlock) exprParseVis.visit(ctx.block()) : null);
  }

  // ====================================================================================
  // Enum
  // ====================================================================================
  @Override
  public AlloyEnumPara visitEnumPara(DashParser.EnumParaContext ctx) {
    return new AlloyEnumPara(
        null != ctx.PRIVATE(),
        (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
        null != ctx.qnames()
            ? visitAll(ctx.qnames().qname(), exprParseVis, AlloyQnameExpr.class)
            : Collections.emptyList());
  }

  // ====================================================================================
  // Fact
  // ====================================================================================
  @Override
  public AlloyFactPara visitFactPara(DashParser.FactParaContext ctx) {
    if (null != ctx.qname()) {
      return new AlloyFactPara(
          new Pos(ctx),
          (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
          (AlloyBlock) this.exprParseVis.visit(ctx.block()));
    } else if (null != ctx.STRING_LITERAL()) {
      return new AlloyFactPara(
          new Pos(ctx),
          new AlloyStrLiteralExpr(new Pos(ctx), ctx.STRING_LITERAL().getText()),
          (AlloyBlock) this.exprParseVis.visit(ctx.block()));
    } else {
      return new AlloyFactPara(new Pos(ctx), (AlloyBlock) this.exprParseVis.visit(ctx.block()));
    }
  }

  // ====================================================================================
  // Pred
  // ====================================================================================
  @Override
  public AlloyPredPara visitPredPara(DashParser.PredParaContext ctx) {
    List<AlloyDecl> decls = Collections.emptyList();
    if (null != ctx.arguments()) {
      if (null != ctx.arguments().decls()) {
        decls = visitAll(ctx.arguments().decls().decl(), exprParseVis, AlloyDecl.class);
      }
    }

    return new AlloyPredPara(
        new Pos(ctx),
        null != ctx.PRIVATE(),
        (null != ctx.sigRef()) ? (AlloySigRefExpr) exprParseVis.visit(ctx.sigRef()) : null,
        (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
        decls,
        (AlloyBlock) exprParseVis.visit(ctx.block()));
  }

  // ====================================================================================
  // Fun
  // ====================================================================================
  @Override
  public AlloyFunPara visitFunPara(DashParser.FunParaContext ctx) {
    List<AlloyDecl> decls = Collections.emptyList();
    if (null != ctx.arguments()) {
      if (null != ctx.arguments().decls()) {
        decls = visitAll(ctx.arguments().decls().decl(), exprParseVis, AlloyDecl.class);
      }
    }

    AlloyFunPara.Mul mul = AlloyFunPara.Mul.DEFAULTSET;
    if (null != ctx.multiplicity()) {
      if (null != ctx.multiplicity().LONE()) {
        mul = AlloyFunPara.Mul.LONE;
      } else if (null != ctx.multiplicity().SOME()) {
        mul = AlloyFunPara.Mul.SOME;
      } else if (null != ctx.multiplicity().ONE()) {
        mul = AlloyFunPara.Mul.ONE;
      } else if (null != ctx.multiplicity().SET()) {
        mul = AlloyFunPara.Mul.SET;
      } else {
        throw AlloyASTImplError.invalidCase(new Pos(ctx));
      }
    }

    return new AlloyFunPara(
        new Pos(ctx),
        null != ctx.PRIVATE(),
        (null != ctx.sigRef()) ? (AlloySigRefExpr) exprParseVis.visit(ctx.sigRef()) : null,
        (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
        decls,
        mul,
        exprParseVis.visit(ctx.expr1()),
        (AlloyBlock) exprParseVis.visit(ctx.block()));
  }

  // ====================================================================================
  // Assert
  // ====================================================================================
  @Override
  public AlloyAssertPara visitAssertPara(DashParser.AssertParaContext ctx) {
    if (null != ctx.qname()) {
      return new AlloyAssertPara(
          new Pos(ctx),
          (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
          (AlloyBlock) this.exprParseVis.visit(ctx.block()));
    } else if (null != ctx.STRING_LITERAL()) {
      return new AlloyAssertPara(
          new Pos(ctx),
          new AlloyStrLiteralExpr(new Pos(ctx), ctx.STRING_LITERAL().getText()),
          (AlloyBlock) this.exprParseVis.visit(ctx.block()));
    } else {
      return new AlloyAssertPara(new Pos(ctx), (AlloyBlock) this.exprParseVis.visit(ctx.block()));
    }
  }

  // ====================================================================================
  // Macro
  // ====================================================================================
  @Override
  public AlloyMacroPara visitMacroPara(DashParser.MacroParaContext ctx) {
    if (null != ctx.block()) {
      return new AlloyMacroPara(
          new Pos(ctx),
          null != ctx.PRIVATE(),
          (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
          null != ctx.qnames()
              ? visitAll(ctx.qnames().qname(), exprParseVis, AlloyQnameExpr.class)
              : Collections.emptyList(),
          (AlloyBlock) exprParseVis.visit(ctx.block()));
    } else if (null != ctx.expr1()) {
      return new AlloyMacroPara(
          new Pos(ctx),
          null != ctx.PRIVATE(),
          (AlloyQnameExpr) exprParseVis.visit(ctx.qname()),
          null != ctx.qnames()
              ? visitAll(ctx.qnames().qname(), exprParseVis, AlloyQnameExpr.class)
              : Collections.emptyList(),
          exprParseVis.visit(ctx.expr1()));
    } else {
      throw AlloyASTImplError.invalidCase(new Pos(ctx));
    }
  }

  // ====================================================================================
  // Command
  // ====================================================================================
  @Override
  public AlloyCmdPara visitCommandPara(DashParser.CommandParaContext ctx) {
    return new AlloyCmdPara(
        new Pos(ctx),
        visitAll(ctx.commandDecl(), new AlloyCmdDeclParseVis(), AlloyCmdPara.CommandDecl.class));
  }
}
