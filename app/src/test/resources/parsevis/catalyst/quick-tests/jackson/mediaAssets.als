module chapter6/mediaAssets

sig ApplicationState {
	catalogs: set Catalog,
	catalogState: catalogs -> one CatalogState,
	currentCatalog: catalogs,
	buffer: set Asset
	}

sig Catalog, Asset {}

sig CatalogState {
	assets: set Asset,
	disj hidden, showing: set assets,
	selection: set assets + Undefined
	} {
	hidden+showing = assets
	}

one sig Undefined {}

pred catalogInv [cs: CatalogState] {
	cs.selection = Undefined or (some cs.selection and cs.selection in cs.showing)
	}

pred appInv [xs: ApplicationState] {
	all cs: xs.catalogs | catalogInv [xs.catalogState[cs]]
	}

pred showSelected [cs, csPrime: CatalogState] {
	cs.selection != Undefined
	csPrime.showing = cs.selection
	csPrime.selection = cs.selection
	csPrime.assets = cs.assets
	}

pred hideSelected [cs, csPrime: CatalogState] {
	cs.selection != Undefined
	csPrime.hidden = cs.hidden + cs.selection
	csPrime.selection = Undefined
	csPrime.assets = cs.assets
	}

pred cut [xs, xsPrime: ApplicationState] {
	let cs = xs.currentCatalog.(xs.catalogState), sel = cs.selection {
		sel != Undefined
		xsPrime.buffer = sel
		some csPrime: CatalogState {
			csPrime.assets = cs.assets - sel
			csPrime.showing = cs.showing - sel
			csPrime.selection = Undefined
			xsPrime.catalogState = xs.catalogState ++ xs.currentCatalog -> csPrime
			}
		}
	xsPrime.catalogs = xs.catalogs
	xsPrime.currentCatalog = xs.currentCatalog
	}

pred paste [xs, xsPrime: ApplicationState] {
	let cs = xs.currentCatalog.(xs.catalogState), buf = xs.buffer {
		xsPrime.buffer = buf
		some csPrime: CatalogState {
			csPrime.assets = cs.assets + buf
			csPrime.showing = cs.showing + (buf - cs.assets)
			csPrime.selection = buf - cs.assets
			xsPrime.catalogState = xs.catalogState ++ xs.currentCatalog -> csPrime
			}
		}
	xsPrime.catalogs = xs.catalogs
	xsPrime.currentCatalog = xs.currentCatalog
	}

assert HidePreservesInv {
	all cs, csPrime: CatalogState |
		catalogInv [cs] and hideSelected [cs, csPrime] => catalogInv [csPrime]
	}

// This check should not find any counterexample
check HidePreservesInv

pred sameApplicationState [xs, xsPrime: ApplicationState] {
	xsPrime.catalogs = xs.catalogs
	all c: xs.catalogs | sameCatalogState [c.(xs.catalogState), c.(xsPrime.catalogState)]
	xsPrime.currentCatalog = xs.currentCatalog
	xsPrime.buffer = xs.buffer
	}

pred sameCatalogState [cs, csPrime: CatalogState] {
	csPrime.assets = cs.assets
	csPrime.showing = cs.showing
	csPrime.selection = cs.selection
	}

assert CutPaste {
	all xs, xsPrime, xs": ApplicationState |
		(appInv [xs] and cut [xs, xsPrime] and paste [xsPrime, xs"]) => sameApplicationState [xs, xs"] 
	}

// This check should find a counterexample
check CutPaste

assert PasteCut {
	all xs, xsPrime, xs": ApplicationState |
		(appInv [xs] and paste [xs, xsPrime] and cut [xsPrime, xs"]) => sameApplicationState [xs, xs"] 
	}

// This check should find a counterexample
check PasteCut

assert PasteNotAffectHidden {
	all xs, xsPrime: ApplicationState |
		(appInv [xs] and paste [xs, xsPrime]) => 
			let c = xs.currentCatalog | xsPrime.catalogState[c].hidden = xs.catalogState[c].hidden
	}

// This check should not find any counterexample
check PasteNotAffectHidden
