package ca.uwaterloo.watform.tlamodel;

public class TreeShaker {
	public static TlaModel removeUnusedDefns(TlaModel tlaModel)
	{
		TlaModel answer = new TlaModel(tlaModel.name, tlaModel.getInit(), tlaModel.getNext());

		return answer;
	}
}
