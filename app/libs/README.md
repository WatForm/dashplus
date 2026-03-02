The org.alloytools.alloy.dist.jar is a custom built Alloy 6 Jar from the
srcs at https://github.com/AlloyTools/org.alloytools.alloy/releases/tag/v6.2.0

Built on 2026-03-02; compiled with Java 17.0.16

with the following modification to Alloy 6.2.0:

* the addition of the following Dash util files:
	- buffer.als
	- ring.als
	- tcmc_path.als
	- tcmc_subgraph.als
	- tcmc.als
	- tcmcfc_path.als
	- tcmc_subgraph.als
	- tcmcfc.als
	- traces.als (from: Alcino Cunha. Bounded model checking of temporal formulas with Alloy. In Lecture Notes in Computer Science, pages 303–308. Springer Berlin Heidelberg, 2014.)
  into org.alloytools.alloy/org.alloytools.alloy.core/src/main/resources/models/util

* changes in translator.A4Solution, translator.ScopeComputer, translator.TranslateAlloyToKodkod
	- these are changes ported from Portus' version of Alloy to make it possible to evaluate the 
	formula of a model in an instance from XML
	- search for 2026-03-02 NAD
	- make various items public
	- added some methods (getFullFormula, evalModel, model2kodkod)
	- added a constructor for TranslateAlloyToKodkod



