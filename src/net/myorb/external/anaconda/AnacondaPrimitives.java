
package net.myorb.external.anaconda;

import net.myorb.math.expressions.algorithms.CommonOperatorLibrary;
import net.myorb.math.expressions.symbols.AbstractParameterizedFunction;

/**
 * primitives from Anaconda library exported for CalcLib use
 * @param <T> manager for data type
 * @author Michael Druckman
 */
public class AnacondaPrimitives<T>
		extends CommonOperatorLibrary<T>
{


/*
 * 		Zeta function
 */

	/**
	 * implement operator - Zeta
	 * @param symbol the symbol associated with this object
	 * @return operation implementation object
	 */
	public AbstractParameterizedFunction getZetaAlgorithm (String symbol)
	{ return new MultipleVectoredMarshalingWrapper (symbol, getZetaImplementation ()); }
	public CommonVectoredFunctionImplementation getZetaImplementation ()
	{ return new Missing ("Zeta"); }

/*
 * 		Gamma function
 */

	/**
	 * implement operator - Gamma
	 * @param symbol the symbol associated with this object
	 * @return operation implementation object
	 */
	public AbstractParameterizedFunction getGammaAlgorithm (String symbol)
	{ return new MultipleVectoredMarshalingWrapper (symbol, getGammaImplementation ()); }
	public CommonVectoredFunctionImplementation getGammaImplementation ()
	{ return new Missing ("Gamma"); }

/*
 * 		Loggamma function
 */

	/**
	 * implement operator - Loggamma
	 * @param symbol the symbol associated with this object
	 * @return operation implementation object
	 */
	public AbstractParameterizedFunction getLoggammaAlgorithm (String symbol)
	{ return new MultipleVectoredMarshalingWrapper (symbol, getLoggammaImplementation ()); }
	public CommonVectoredFunctionImplementation getLoggammaImplementation ()
	{ return new Missing ("Loggamma"); }

/*
 * 		Polygamma function
 */

	/**
	 * implement operator - Polygamma
	 * @param symbol the symbol associated with this object
	 * @return operation implementation object
	 */
	public AbstractParameterizedFunction getPolygammaAlgorithm (String symbol)
	{ return new MultipleVectoredMarshalingWrapper (symbol, getPolygammaImplementation ()); }
	public CommonVectoredFunctionImplementation getPolygammaImplementation ()
	{ return new Missing ("Polygamma"); }

/*
 * 		Polylog function
 */

	/**
	 * implement operator - Polylog
	 * @param symbol the symbol associated with this object
	 * @return operation implementation object
	 */
	public AbstractParameterizedFunction getPolylogAlgorithm (String symbol)
	{ return new MultipleVectoredMarshalingWrapper (symbol, getPolylogImplementation ()); }
	public CommonVectoredFunctionImplementation getPolylogImplementation ()
	{ return new Missing ("Polylog"); }

/*
 * 		Omega function
 */

	/**
	 * implement operator - Omega
	 * @param symbol the symbol associated with this object
	 * @return operation implementation object
	 */
	public AbstractParameterizedFunction getOmegaAlgorithm (String symbol)
	{ return new MultipleVectoredMarshalingWrapper (symbol, getOmegaImplementation ()); }
	public CommonVectoredFunctionImplementation getOmegaImplementation ()
	{ return new Missing ("Omega"); }

}
