The org.alloytools.alloy.dist.jar is a custom built Alloy 6 Jar from 
https://github.com/AlloyTools/org.alloytools.alloy (downloaded 2026-02-19).

The only modification to the Alloy 6 Jar is the addition of the following Dash util files:


* buffer.als
* ring.als
* tcmc_path.als
* tcmc_subgraph.als
* tcmc.als
* tcmcfc_path.als
* tcmc_subgraph.als
* tcmcfc.als
* traces.als (from: Alcino Cunha. Bounded model checking of temporal formulas with Alloy. In Lecture Notes in Computer Science, pages 303–308. Springer Berlin Heidelberg, 2014.)

into org.alloytools.alloy/org.alloytools.alloy.core/src/main/resources/models/util

