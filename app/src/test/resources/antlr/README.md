# Change Alloy 4 to test Antlr grammar
- primed variables used as qnames (allowed in Alloy 4) are changed to 'xPrime' for the purpose of testing the antlr grammar
`
for f in *.als; do
  sed -i '' -E "s/([a-z])'/\1Prime/g" "$f"
done
`

- 'steps' is a keyword in Alloy and cannot be used as a module name

