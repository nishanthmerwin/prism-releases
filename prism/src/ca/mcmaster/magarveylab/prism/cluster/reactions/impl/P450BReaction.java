package ca.mcmaster.magarveylab.prism.cluster.reactions.impl;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

import ca.mcmaster.magarveylab.enums.domains.DomainType;
import ca.mcmaster.magarveylab.enums.domains.TailoringDomains;
import ca.mcmaster.magarveylab.prism.cluster.reactions.Reaction;
import ca.mcmaster.magarveylab.prism.cluster.reactions.GenericReaction;
import ca.mcmaster.magarveylab.prism.cluster.reactions.UtilityReactions;
import ca.mcmaster.magarveylab.prism.cluster.scaffold.Chemoinformatics.Atoms;
import ca.mcmaster.magarveylab.prism.data.Cluster;
import ca.mcmaster.magarveylab.prism.data.Module;
import ca.mcmaster.magarveylab.prism.data.reactions.ReactionPlan;
import ca.mcmaster.magarveylab.prism.data.structure.Residue;
import ca.mcmaster.magarveylab.prism.data.structure.Scaffold;
import ca.mcmaster.magarveylab.prism.util.exception.NoResidueException;
import ca.mcmaster.magarveylab.prism.util.exception.ScaffoldGenerationException;
import ca.mcmaster.magarveylab.prism.util.exception.TailoringSubstrateException;

public class P450BReaction extends GenericReaction implements Reaction {

	public P450BReaction(ReactionPlan plan, Scaffold scaffold, Cluster cluster) {
		super(plan, scaffold, cluster);
		this.domains = new DomainType[] { TailoringDomains.P450B };
	}
	
	public void execute() throws NoResidueException, TailoringSubstrateException, ScaffoldGenerationException {
		Module firstModule = plan.get(0);
		Module secondModule = plan.get(1);
		Residue firstResidue = scaffold.residue(firstModule);
		Residue secondResidue = scaffold.residue(secondModule);
		IAtomContainer first = firstResidue.structure();
		IAtomContainer second = secondResidue.structure();

		IAtomContainer molecule = scaffold.molecule();
		
		// get tyrosine -OH
		IAtom hydroxylCarbon = Atoms.getAromaticHydroxylCarbon(first);
		IAtom oxygen = Atoms.getConnectedOxygen(hydroxylCarbon, molecule);
		
		// get phenylglycine C
		IAtom paraCarbon = Atoms.getAromaticHydroxylCarbon(second);
		IAtom carbon = null;
		for (IAtom atom : second.getConnectedAtomsList(paraCarbon)) 
			if (atom.getSymbol().equals("C") && molecule.getConnectedBondsCount(atom) == 2)
				carbon = atom;
		
		// add bond
		if (molecule.getBond(oxygen, carbon) == null)
			UtilityReactions.addBond(oxygen, carbon, molecule);
	}
	
}
