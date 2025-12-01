#!/bin/bash
set -e  

echo "Downloading zip"
rm catalyst-corpus.zip
curl -L -o catalyst-corpus.zip https://github.com/WatForm/dashplus/releases/download/catalyst-corpus-v1/catalyst-corpus.zip

echo "Unzipping"
rm -rf ./catalyst-corpus
unzip catalyst-corpus.zip &> /dev/null

# Move problematic files/folders
echo "Removing & moving problematic files"
rm -rf ./problemFiles
mkdir -p ./problemFiles
# Many files in this dir fails (CUP pass, Antlr fail) because CUP accepts $ in command names, but I think we shouldn't
mv ./catalyst-corpus/bnwxqtiettobmisgmtydwqdhmp63kwbf-MFIS/*  problemFiles
# Alloy analyzer will accept predicate paragraphs with the same name (not overloaded)
# and only complain when they are invoked. But in Dashplus, we will throw at their declarations
mv ./catalyst-corpus/ium4462f6j3ebjvu7y6at3knjtbptki7-fm4UnsatCoresAlloy/src/main/resources/testsigpred.als problemFiles
mv ./catalyst-corpus/nxa4mvqsxxmcdgjbytweikcijqhdvsce-Alloy4FunDataAnalysis/SubmissionsByClassification/trash_ltl/mod/xiJ9ipczDBYRLXMum.als problemFiles
mv ./catalyst-corpus/nxa4mvqsxxmcdgjbytweikcijqhdvsce-Alloy4FunDataAnalysis/SubmissionsByClassification/socialMedia/mod/iLrtx5hnC8DZm2szu.als problemFiles
mv ./catalyst-corpus/nxa4mvqsxxmcdgjbytweikcijqhdvsce-Alloy4FunDataAnalysis/SubmissionsByClassification/courses_v1/mod/KwLs762a3de4sWcPD.als problemFiles
mv ./catalyst-corpus/nxa4mvqsxxmcdgjbytweikcijqhdvsce-Alloy4FunDataAnalysis/SubmissionsByClassification/courses_v1/mod/C6WDSaicQJTuGYFBo.als problemFiles
mv ./catalyst-corpus/nxa4mvqsxxmcdgjbytweikcijqhdvsce-Alloy4FunDataAnalysis/SubmissionsByClassification/lts/mod/QjysAxXLP9AZomQBA.als problemFiles
mv ./catalyst-corpus/nxa4mvqsxxmcdgjbytweikcijqhdvsce-Alloy4FunDataAnalysis/SubmissionsByClassification/courses_v2/mod/6kHA6G93hcdwmKSdg.als problemFiles
mv ./catalyst-corpus/nxa4mvqsxxmcdgjbytweikcijqhdvsce-Alloy4FunDataAnalysis/SubmissionsByClassification/courses_v2/mod/8vTFBPvCZzW3eeDKF.als problemFiles

# Move files that might timeout during JUnit testing
# These can be parsed when testing individually, with longer timeouts, but they cause problems when ran in bulk
rm -rf ./timeout
mkdir -p ./timeout

mv ./catalyst-corpus/2ptebqk5wi27fer6grt3zredhxavfy6c-Alloy-Magic-Card/alloy-models/database_generated.als \
   ./timeout

mv ./catalyst-corpus/375n6r5ozcw6jh644kyoartwzyd5kapg-Alloy4PA/generated/Benchmarks/B2/EscrowVault+EPA/EscrowVault+EPA.als \
   ./timeout

mv  ./catalyst-corpus/bptxoa6g7visqsbv6qkpbdmvfwqsq2or-predicate-abstraction-for-smart-contract-validation/Benchmark#2/RefundEscrow/Alloy/RefundEscrow_EPA.als \
	./timeout

mv  ./catalyst-corpus/bptxoa6g7visqsbv6qkpbdmvfwqsq2or-predicate-abstraction-for-smart-contract-validation/Benchmark#2/ValidatorAuction/Alloy/ValidatorAuction_withdrawHighestB.als \
	./timeout

mv  ./catalyst-corpus/bptxoa6g7visqsbv6qkpbdmvfwqsq2or-predicate-abstraction-for-smart-contract-validation/Benchmark#2/EscrowVault/Alloy/EscrowVault_EPA.als \
	./timeout

mv  ./catalyst-corpus/bptxoa6g7visqsbv6qkpbdmvfwqsq2or-predicate-abstraction-for-smart-contract-validation/Benchmark#2/EPXCrowdsale/Alloy/EPXCrowdsale_EPA_isCrowdSaleClosed_2.als \
	./timeout

mv  ./catalyst-corpus/bptxoa6g7visqsbv6qkpbdmvfwqsq2or-predicate-abstraction-for-smart-contract-validation/Benchmark#2/EPXCrowdsale/Alloy/EPXCrowdsale_EPA_isCrowdSaleClosed.als \
	./timeout

mv ./catalyst-corpus/bptxoa6g7visqsbv6qkpbdmvfwqsq2or-predicate-abstraction-for-smart-contract-validation/Benchmark#2/EPXCrowdsale/Alloy/EPXCrowdsale_EPA.als \
	./timeout

mv ./catalyst-corpus/bptxoa6g7visqsbv6qkpbdmvfwqsq2or-predicate-abstraction-for-smart-contract-validation/Benchmark#2/EPXCrowdsale/Alloy/EPXCrowdsale.als \
	./timeout

mv ./catalyst-corpus/bptxoa6g7visqsbv6qkpbdmvfwqsq2or-predicate-abstraction-for-smart-contract-validation/Benchmark#2/ValidatorAuction/Alloy/ValidatorAuction_EPA.als \
	./timeout

mv ./catalyst-corpus/bptxoa6g7visqsbv6qkpbdmvfwqsq2or-predicate-abstraction-for-smart-contract-validation/Benchmark#2/Crowdfunding/Alloy/Crowdfunding_EPA_time_timeInActions.als \
	./timeout

mv ./catalyst-corpus/bptxoa6g7visqsbv6qkpbdmvfwqsq2or-predicate-abstraction-for-smart-contract-validation/Benchmark#2/Crowdfunding/Alloy/Crowdfunding_EPA_balance_allstates.als \
	./timeout

mv "./catalyst-corpus/xaocutatoyw7clukzttoo3cparj2obze-pewa-tool/bin/Integerm32.als" problemFiles/
mv "./catalyst-corpus/xaocutatoyw7clukzttoo3cparj2obze-pewa-tool/bin/specs-wa/Integer32.als" problemFiles/
mv "./catalyst-corpus/xaocutatoyw7clukzttoo3cparj2obze-pewa-tool/examples/list/actions (copia)/Integer32.als" problemFiles/
mv "./catalyst-corpus/xaocutatoyw7clukzttoo3cparj2obze-pewa-tool/examples/list/models/Integer32.als" problemFiles/

echo "Catalyst corpus setup completed."

