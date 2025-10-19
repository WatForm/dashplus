package ca.uwaterloo.watform.dashtotlaplus;

import java.io.IOException;
import java.nio.file.Paths;

import ca.uwaterloo.watform.tlaplusast.*;
import ca.uwaterloo.watform.tlaplusmodel.*;

public class Main {
	public static void main(String[] args) throws Exception {
		
		System.out.println("Hello from the dashtotlaplus class");

		TLAPlusModule module = new TLAPlusModule("test");

		System.out.println(module.code());

	}
}
