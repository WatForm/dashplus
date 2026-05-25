package ca.uwaterloo.watform.tlaast;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import ca.uwaterloo.watform.tlaast.TlaTypes.Base;
import ca.uwaterloo.watform.tlaast.TlaTypes.Compound;
import java.util.Arrays;
import java.util.List;

public class TlaTypes {

    // this follows the type annotations used by Snowcat in Apalache

    public abstract static class Type {
        public abstract String TlaSnippet();

        public String annotation() {
            return "\\* @type: " + this.TlaSnippet() + ";";
        }
    }

    public static class Base extends Type {
        public final String name;

        public Base(String name) {
            this.name = name;
        }

        @Override
        public String TlaSnippet() {
            return this.name;
        }
    }

    public static class Compound extends Type {
        public final String name;
        public final List<Type> children;

        public Compound(String name, List<Type> children) {
            this.name = name;
            this.children = children;
        }

        @Override
        public String TlaSnippet() {
            return name + "(" + strCommaList(mapBy(this.children, c -> c.TlaSnippet())) + ")";
        }
    }

    public static Type Int() {
        return new Base("Int");
    }

    public static Type Str() {
        return new Base("Str");
    }

    public static Type Bool() {
        return new Base("Bool");
    }

    public static Type Seq(Type t) {
        return new Compound("Seq", Arrays.asList(t));
    }

    public static Type Set(Type t) {
        return new Compound("Set", Arrays.asList(t));
    }

    public static class Function extends Type {
        Type domain;
        Type range;

        public Function(Type domain, Type range) {
            this.domain = domain;
            this.range = range;
        }

        @Override
        public String TlaSnippet() {
            return this.domain.TlaSnippet() + " -> " + this.range.TlaSnippet();
        }
    }
}
