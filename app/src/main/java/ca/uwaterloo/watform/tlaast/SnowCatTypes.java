package ca.uwaterloo.watform.tlaast;

import static ca.uwaterloo.watform.utils.GeneralUtil.*;

import java.util.*;

public class SnowCatTypes {

	// https://apalache-mc.org/docs/adr/002adr-types.html#1-how-to-write-types-in-tla

	public abstract static class SCType {
    public abstract String core();
    public String annotation() {
      return "\\* @type: " + this.core() + ";";
    }
  }

  private static class BaseType extends SCType {
	private final String name;
	BaseType(String name)
	{
		this.name = name;
	}
	@Override
	public String core() {
		return this.name;
	}
  }


  private static class UnaryConstructedType extends SCType {
	private final SCType child;
	private final String name;
	UnaryConstructedType(String name, SCType child) {
		this.child = child;
		this.name = name;
	}
	@Override
	public String core() {
		return this.name + "(" + this.child.core() + ")";
	}
  }

  private static class FunctionType extends SCType {

	private final SCType domainType;
	private final SCType rangetype;
	FunctionType(SCType domainType, SCType rangeType)
	{
		this.domainType = domainType;
		this.rangetype = rangeType;
	}
	@Override
	public String core() {
		return this.domainType.core() + " -> "+ this.rangetype.core();
	}
  }

  private static class TupleType extends SCType {

	private final List<SCType> children;
	TupleType(List<SCType> children)
	{
		this.children = children;
	}
	@Override
	public String core() {
		return "<<" + strCommaList(mapBy(this.children, c -> c.core())) + ">>";
	}
	
  }

  private static class OperatorType extends SCType {

	private final List<SCType> argumentTypes;
	private final SCType returnType;
	OperatorType(List<SCType> argumentTypes, SCType returnType)
	{
		this.argumentTypes = argumentTypes;
		this.returnType = returnType;
	}
	@Override
	public String core() {
		return "(" + strCommaList(mapBy(this.argumentTypes, t -> t.core())) + ") => " + this.returnType.core();
	}
	
  }

  public static SCType Str()
  {
	return new BaseType("Str");
  }
  public static SCType Bool()
  {
	return new BaseType("Bool");
  }
  public static SCType Int()
  {
	return new BaseType("Int");
  }
  public static SCType Set(SCType t)
  {
	return new UnaryConstructedType("Set", t);
  }
  public static SCType Seq(SCType t)
  {
	return new UnaryConstructedType("Seq", t);
  }
  public static SCType Parentheses(SCType t)
  {
	return new UnaryConstructedType("", t);
  }
  public static SCType Function(SCType domain, SCType range)
  {
	return new FunctionType(domain, range);
  }
  public static SCType Tuple(List<SCType> children)
  {
	return new TupleType(children);
  }
  public static SCType BinaryTuple(SCType T)
  {
	return new TupleType(Arrays.asList(T,T));
  }
  public static SCType Operator(List<SCType> argumentTypes, SCType resultType)
  {
	return new OperatorType(argumentTypes, resultType);
  }
  public static SCType OperatorT2T(SCType T)
  {
	return new OperatorType(Arrays.asList(T), T);
  }
  public static SCType OperatorU2V(SCType U, SCType V)
  {
	return new OperatorType(Arrays.asList(U), V);
  }
  public static SCType OperatorTT2T(SCType T)
  {
	return new OperatorType(Arrays.asList(T,T), T);
  }
	
}
