#!/bin/bash
set -e  

echo "Downloading zip"
curl -L -o catalyst-corpus.zip https://github.com/QiyueChen04/dashplus/releases/download/catalyst-corpus-v1/catalyst-corpus.zip

echo "Unzipping"
unzip catalyst-corpus.zip &> /dev/null

echo "Removing & moving problematic files"
# Remove problematic files/folders
# many files in this dir fails (CUP pass, Antlr fail) because CUP accepts $ in command names, but I think we shouldn't
rm -rf ./catalyst-corpus/bnwxqtiettobmisgmtydwqdhmp63kwbf-MFIS/*

# Move files that might timeout during JUnit testing
# These can be parsed quickly when testing individually
mkdir -p ./timeout

mv ./catalyst-corpus/xaocutatoyw7clukzttoo3cparj2obze-pewa-tool/bin/Integerm32.als \
   ./timeout

mv ./catalyst-corpus/2ptebqk5wi27fer6grt3zredhxavfy6c-Alloy-Magic-Card/alloy-models/database_generated.als \
   ./timeout

mv ./catalyst-corpus/375n6r5ozcw6jh644kyoartwzyd5kapg-Alloy4PA/generated/Benchmarks/B2/EscrowVault+EPA/EscrowVault+EPA_part6.als \
   ./timeout

mv ./catalyst-corpus/375n6r5ozcw6jh644kyoartwzyd5kapg-Alloy4PA/generated/Benchmarks/B2/EscrowVault+EPA/EscrowVault+EPA.als \
   ./timeout

echo "Catalyst corpus setup completed."

