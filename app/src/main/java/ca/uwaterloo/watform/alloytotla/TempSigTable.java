package ca.uwaterloo.watform.alloytotla;

import java.util.*;

public class TempSigTable {
	static class Sig
	{
		String name;
		boolean isAbstract;
		Sig extendsParent;
		Sig inParent;
		public Sig(String name)
		{
			this.name = name;
			this.isAbstract = false;
			this.extendsParent = null;
			this.inParent = null;
		}
	}
	List<Sig> listSigs;
	public TempSigTable(){
		listSigs = new ArrayList<>();

	}
	public void testOne()
	{
		Sig A = new Sig("A");
		Sig B = new Sig("B");
		Sig C = new Sig("C");
		this.listSigs.add(A);
		this.listSigs.add(B);
		this.listSigs.add(C);
	}
}
