package ca.uwaterloo.watform.dashtotla;

import java.util.logging.Logger;

import ca.uwaterloo.watform.alloytotla.BaseA2T;
import ca.uwaterloo.watform.dashmodel.DashModel;
import ca.uwaterloo.watform.tlamodel.TlaModel;
import ca.uwaterloo.watform.utils.CustomLoggerFactory;

public class BaseD2T {

	public final DashModel dashModel;
	public final TlaModel tlaModel;
	public final boolean verbose;
	public final boolean debug;
	public final Logger l;

	public BaseD2T(DashModel dashModel, TlaModel tlaModel, boolean verbose, boolean debug)
	{
		this.dashModel = dashModel;
		this.tlaModel = tlaModel;
		this.verbose = verbose;
		this.debug = debug;
		this.l = CustomLoggerFactory.make("DashToTla",debug);
	}
	
}
