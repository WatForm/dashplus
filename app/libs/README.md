The org.alloytools.alloy.dist.jar is a custom built Alloy 6 Jar from the
srcs at https://github.com/AlloyTools/org.alloytools.alloy/releases/tag/v6.2.0 (org.alloytools.alloy-6.2.0.zip) put into a tmp directory

mkdir tmp
cd tmp
wget https://github.com/AlloyTools/org.alloytools.alloy/archive/refs/tags/v6.2.0.zip
unzip v6.2.0.zip
cp ../extra-util-files/* org.alloytools.alloy-6.2.0/org.alloytools.alloy.core/src/main/resources/models/util/
cd org.alloytools.alloy-6.2.0/org.alloytools.alloy.core/src/main/resources/models/util/

with the following modification to Alloy 6.2.0:

* in tmp/org.alloytools.alloy-6.2.0/org.alloytools.alloy.core/src/main/resources/models/util, the addition of the following Dash util files:
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

and uses of next/prev in with (this/next) and (this/prev) 
  org.alloytools.alloy-6.2.0/org.alloytools.alloy.core/src/main/resources/models/util/integer.als:
	e.g., fun prev:Int->Int { ~next }
	with fun prev:Int->Int { ~this/next }
	to avoid problems with overloading temporarily

./gradlew build

Built on 2026-03-12 (./gradlew build); compiled with Java 17.0.18


mv org.alloytools.alloy.dist/target/org.alloytools.alloy.dist.jar ../..

Remember to rebuild dashplus!

