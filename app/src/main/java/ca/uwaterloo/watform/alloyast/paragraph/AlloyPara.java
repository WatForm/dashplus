package ca.uwaterloo.watform.alloyast.paragraph;

import ca.uwaterloo.watform.alloyast.AlloyASTNode;
import ca.uwaterloo.watform.utils.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AlloyPara extends AlloyASTNode {
    public AlloyPara(Pos pos) {
        super(pos);
    }

    public AlloyPara() {
        super();
    }

    public abstract AlloyId getId();

    public static final class AlloyId {
        public final String name;
        public final List<String> args;

        // the purpose of args is for pred/fun overloading
        // the types of the args
        // ex:
        // p[a: Int, b: Int]
        // name = "p"
        // args = ["Int", "Int"]

        // for predicate and function
        public AlloyId(String name, List<String> args) {
            this.name = name;
            this.args = List.copyOf(args);
        }

        // for everything else
        public AlloyId(String name) {
            this.name = name;
            this.args = Collections.emptyList();
        }

        // for empty
        public AlloyId() {
            this.name = "";
            this.args = Collections.emptyList();
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name, this.args);
        }

        // the order of this.args matters
        // this is the same behaviour as Alloy Analyzer
        // so p[a:A, b:B] != p[b:B, a:A]
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            AlloyId other = (AlloyId) obj;
            if (name == null) {
                if (other.name != null) return false;
            } else if (!name.equals(other.name)) return false;
            if (args == null) {
                if (other.args != null) return false;
            } else if (!args.equals(other.args)) return false;
            return true;
        }
    }
}
