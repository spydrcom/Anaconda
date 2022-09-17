
package net.myorb.external.anaconda;

import net.myorb.math.complexnumbers.ComplexValue;
import net.myorb.math.complexnumbers.ComplexMarker;

import net.myorb.math.complexnumbers.PythonComplexFunctionRPC;

import net.myorb.math.expressions.gui.rendering.NodeFormatting;
import net.myorb.math.expressions.TypedRangeDescription.TypedRangeProperties;
import net.myorb.math.expressions.evaluationstates.Environment;

import net.myorb.charting.DisplayGraphTypes.Point.Series;
import net.myorb.data.abstractions.ServerAccess;
import net.myorb.utilities.Configurable;

import java.util.List;

/**
 * primitives from Anaconda library exported for CalcLib use.
 *  primitives implemented for complex number functions; implementation list can be extended;
 *  to extend the list of implemented functions, the generic primitive must be included as well
 * @author Michael Druckman
 */
public class ComplexAnacondaRPC
	extends AnacondaPrimitives<ComplexValue<Double>>
	implements Configurable
{


	/**
	 * text connection and verify service is available
	 */
	void checkAnacondaStatus ()
	{
		try
		{
			String response =
				getAnacondaAccess ().issueRequest
					(ANACONDA_STATUS);
			System.out.println (response);
		}
		catch (Exception e)
		{
			System.out.println ("Anaconda services are not currently available");
		}
	}
	static final String ANACONDA_STATUS = "getFtuples ( mpmath.zeta, 0.5, 33, 1.0, 0.0, 1 )";


	/* (non-Javadoc)
	 * @see net.myorb.utilities.Configurable#configure(java.lang.String)
	 */
	public void configure (String text)
	{
		String[] items = text.split (":");
		System.out.println ("Anaconda services expected on " + text);
		port = Integer.parseInt (items[1]); host = items[0];
		checkAnacondaStatus ();
	}

	/**
	 * @return access to Anaconda server
	 */
	static ServerAccess getAnacondaAccess ()
	{
		return new ServerAccess (host, port);
	}
	static String host; static int port;


	/**
	 * Anaconda version of vector-enabled
	 */
	class VectorEnabledFunction extends CommonVectoredFunctionImplementation
	{

		/**
		 * @return the text of the function description used in the call
		 */
		String functionRpcText () { throw new RuntimeException ("Function description for RPC not found"); }

		/* (non-Javadoc)
		 * @see net.myorb.math.expressions.VectorPlotEnabled#evaluateSeries(net.myorb.math.expressions.TypedRangeDescription.TypedRangeProperties, java.util.List, net.myorb.math.expressions.evaluationstates.Environment)
		 */
		public void evaluateSeries
			(
				TypedRangeProperties<ComplexValue<Double>> domainDescription,
				List<Series> series, Environment<ComplexValue<Double>> environment
			)
		{ rpc.evaluateSeries (domainDescription, series, environment); }

		/**
		 * Remote Procedure Call implementation for Python specific protocol
		 */
		protected PythonComplexFunctionRPC rpc = new PythonComplexFunctionRPC ()
		{
			/* (non-Javadoc)
			 * @see net.myorb.math.complexnumbers.PythonComplexFunctionRPC#getServerAccess()
			 */
			public ServerAccess getServerAccess () { return getAnacondaAccess (); }

			/* (non-Javadoc)
			 * @see net.myorb.math.complexnumbers.PythonComplexFunctionRPC#functionReference()
			 */
			public String functionReference () { return functionRpcText (); }
		};

	}


/*
 * 		Lambert W function
 */

	/* (non-Javadoc)
	 * @see net.myorb.math.anaconda.AnacondaPrimitives#getOmegaImplementation()
	 */
	public CommonVectoredFunctionImplementation getOmegaImplementation ()
	{
		return new VectorEnabledFunction ()
		{
			@SuppressWarnings("unchecked")
			public ComplexValue<Double> evaluate (List<ComplexValue<Double>> using)
			{
				ComplexMarker value; int n;
				profile.parameterCheck (n = using.size ());
				value = n==1? omega (using.get (0), Double.parseDouble (configuration)) :
					omega (using.get (0), using.get (1).Re ());
				return (ComplexValue<Double>) value;
			}
			FunctionProfile profile = new FunctionProfile ("LambertW", 1, 2);
	
			/* (non-Javadoc)
			 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#configure(java.lang.String)
			 */
			public void configure (String parameters)
			{
				this.configuration = parameters;
			}
			String configuration = "0";

			/* (non-Javadoc)
			 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#markupForDisplay(java.lang.String, java.lang.String, net.myorb.math.expressions.gui.rendering.NodeFormatting)
			 */
			public String markupForDisplay (String operator, String operand, NodeFormatting using)
			{ return omegaRender (configuration, using) + using.formatParenthetical (operand); }

			/* (non-Javadoc)
			 * @see net.myorb.math.anaconda.ComplexAnacondaRPC.VectorEnabledFunction#functionRpcText()
			 */
			String functionRpcText () { return omegaText (Double.parseDouble (configuration)); }
		};
	}
	ComplexMarker omega
	(ComplexValue<Double> z, double k)
	{ return PythonComplexFunctionRPC.requestEvalFor (getAnacondaAccess (), omegaText (k), z); }
	String omegaText (double k) { return "lambda x: mpmath.lambertw ( x, " + k + " )"; }

	String omegaRender (String order, NodeFormatting using)
	{
		return formatNumericSubscript (formatIdentifierFor ("omega", using), order, using);
	}


/*
 * 		GAMMA
 */

	/* (non-Javadoc)
	 * @see net.myorb.math.anaconda.AnacondaPrimitives#getGammaImplementation()
	 */
	public CommonVectoredFunctionImplementation getGammaImplementation ()
	{
		return new VectorEnabledFunction ()
		{
			@SuppressWarnings("unchecked")
			public ComplexValue<Double> evaluate (List<ComplexValue<Double>> using)
			{
				profile.parameterCheck (using.size ());
				return (ComplexValue<Double>) gamma (using.get (0));
			}
			FunctionProfile profile = new FunctionProfile ("Gamma", 1, 1);
	
			/* (non-Javadoc)
			 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#markupForDisplay(java.lang.String, java.lang.String, net.myorb.math.expressions.gui.rendering.NodeFormatting)
			 */
			public String markupForDisplay (String operator, String operand, NodeFormatting using)
			{ return gammaRender (using) + using.formatParenthetical (operand); }

			/* (non-Javadoc)
			 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#configure(java.lang.String)
			 */
			public void configure (String parameters) {}
	
			/* (non-Javadoc)
			 * @see net.myorb.math.anaconda.ComplexAnacondaRPC.VectorEnabledFunction#functionRpcText()
			 */
			String functionRpcText () { return gammaText (); }
		};
	}
	ComplexMarker gamma (ComplexValue<Double> z)
	{ return PythonComplexFunctionRPC.requestEvalFor (getAnacondaAccess (), gammaText (), z); }
	String gammaRender (NodeFormatting using) { return formatIdentifierFor ("GAMMA", using); }
	String gammaText () { return "mpmath.gamma"; }


/*
 * 		LOGGAMMA
 */

	/* (non-Javadoc)
	 * @see net.myorb.math.anaconda.AnacondaPrimitives#getLoggammaImplementation()
	 */
	public CommonVectoredFunctionImplementation getLoggammaImplementation ()
	{
		return new VectorEnabledFunction ()
		{
			@SuppressWarnings("unchecked")
			public ComplexValue<Double> evaluate (List<ComplexValue<Double>> using)
			{
				profile.parameterCheck (using.size ());
				return (ComplexValue<Double>) loggamma (using.get (0));
			}
			FunctionProfile profile = new FunctionProfile ("Loggamma", 1, 1);
	
			/* (non-Javadoc)
			 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#markupForDisplay(java.lang.String, java.lang.String, net.myorb.math.expressions.gui.rendering.NodeFormatting)
			 */
			public String markupForDisplay (String operator, String operand, NodeFormatting using)
			{ return loggammaRender (using) + using.formatParenthetical (operand); }

			/* (non-Javadoc)
			 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#configure(java.lang.String)
			 */
			public void configure (String parameters) {}
	
			/* (non-Javadoc)
			 * @see net.myorb.math.anaconda.ComplexAnacondaRPC.VectorEnabledFunction#functionRpcText()
			 */
			String functionRpcText () { return logGammaText (); }
		};
	}
	ComplexMarker loggamma (ComplexValue<Double> z)
	{ return PythonComplexFunctionRPC.requestEvalFor (getAnacondaAccess (), logGammaText (), z); }
	String loggammaRender (NodeFormatting using) { return using.formatIdentifierReference ("Ln" + gammaLookup ()); }
	String gammaLookup () { return lookupIdentifierFor ("GAMMA"); }
	String logGammaText () { return "mpmath.loggamma"; }


/*
 * 		POLYGAMMA
 */

	/* (non-Javadoc)
	 * @see net.myorb.math.anaconda.AnacondaPrimitives#getPolygammaImplementation()
	 */
	public CommonVectoredFunctionImplementation getPolygammaImplementation ()
	{
		return new VectorEnabledFunction ()
		{
			@SuppressWarnings("unchecked")
			public ComplexValue<Double> evaluate (List<ComplexValue<Double>> using)
			{
				int n = using.size ();
				profile.parameterCheck (n);
				ComplexValue<Double> z = using.get (0); int m;
				if (n == 1) m = Integer.parseInt (configuration);
				else { m = z.Re ().intValue (); z = using.get (1); }
				return (ComplexValue<Double>) psi (m, z);
			}
			FunctionProfile profile = new FunctionProfile ("Polygamma", 1, 2);
	
			/* (non-Javadoc)
			 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#markupForDisplay(java.lang.String, java.lang.String, net.myorb.math.expressions.gui.rendering.NodeFormatting)
			 */
			public String markupForDisplay (String operator, String operand, NodeFormatting using)
			{ return psiRender (configuration, using) + using.formatParenthetical (operand); }

			/* (non-Javadoc)
			 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#configure(java.lang.String)
			 */
			public void configure (String parameters)
			{ this.configuration = parameters; }
			String configuration = "2";
	
			/* (non-Javadoc)
			 * @see net.myorb.math.anaconda.ComplexAnacondaRPC.VectorEnabledFunction#functionRpcText()
			 */
			String functionRpcText () { return psiText (Integer.parseInt (configuration)); }
		};
	}
	ComplexMarker psi (int m, ComplexValue<Double> z)
	{ return PythonComplexFunctionRPC.requestEvalFor (getAnacondaAccess (), psiText (m), z); }
	String psiText (int m) { return "lambda x: mpmath.polygamma ( " + m + ", x )"; }

	String psiRender (String order, NodeFormatting using)
	{
		return using.formatSuperScript
		(
			formatIdentifierFor ("psi", using),
			using.formatParenthetical
			(
				using.formatNumericReference (order)
			)
		);
	}


/*
 * 		POLYLOG
 */

	/* (non-Javadoc)
	 * @see net.myorb.math.anaconda.AnacondaPrimitives#getPolylogImplementation()
	 */
	public CommonVectoredFunctionImplementation getPolylogImplementation ()
	{
		return new VectorEnabledFunction ()
		{
			@SuppressWarnings("unchecked")
			public ComplexValue<Double> evaluate (List<ComplexValue<Double>> using)
			{
				int n = using.size ();
				profile.parameterCheck (n);
				ComplexValue<Double> p0 = using.get (0), p1;

				if (n == 1)
				{
					ComplexValue<Double> pc =
						new ComplexValue<Double>
						(
							Double.parseDouble (configuration), 0.0,
							p0.getSpaceManager ().getComponentManager ()
						);
					p1 = p0; p0 = pc;
				} else p1 = using.get (1);

				return (ComplexValue<Double>) polylog (p0, p1);
			}
			FunctionProfile profile = new FunctionProfile ("Polylog", 1, 2);

			/* (non-Javadoc)
			 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#markupForDisplay(java.lang.String, java.lang.String, net.myorb.math.expressions.gui.rendering.NodeFormatting)
			 */
			public String markupForDisplay (String operator, String operand, NodeFormatting using)
			{ return polylogRender (configuration, using) + using.formatParenthetical (operand); }

			/* (non-Javadoc)
			 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#configure(java.lang.String)
			 */
			public void configure (String parameters)
			{
				this.configuration = parameters;
			}
			String configuration = "2";
	
			/* (non-Javadoc)
			 * @see net.myorb.math.anaconda.ComplexAnacondaRPC.VectorEnabledFunction#functionRpcText()
			 */
			String functionRpcText () { return plogText (Integer.parseInt (configuration)); }
		};
	}
	ComplexMarker polylog (ComplexValue<Double> order, ComplexValue<Double> z)
	{ return PythonComplexFunctionRPC.requestEvalFor (getAnacondaAccess (), plogText (order.Re ().intValue ()), z); }
	String plogText (int order) { return "lambda x: mpmath.polylog ( " + order + ", x )"; }

	String polylogRender (String order, NodeFormatting using)
	{
		return formatNumericSubscript (using.formatIdentifierReference ("Li"), order, using);
	}


/*
 * 		eta
 */

	/* (non-Javadoc)
	 * @see net.myorb.math.anaconda.AnacondaPrimitives#getEtaImplementation()
	 */
	public CommonVectoredFunctionImplementation getEtaImplementation ()
	{
		return new VectorEnabledFunction ()
		{
			@SuppressWarnings("unchecked")
			public ComplexValue<Double> evaluate (List<ComplexValue<Double>> using)
			{
				ComplexMarker value;
				int n = using.size ();
				profile.parameterCheck (n);
				value = n==1? eta (using.get (0)) : eta (using.get (0), using.get (1));
				return (ComplexValue<Double>) value;
			}
			FunctionProfile profile = new FunctionProfile ("eta", 1, 2);

			/* (non-Javadoc)
			 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#markupForDisplay(java.lang.String, java.lang.String, net.myorb.math.expressions.gui.rendering.NodeFormatting)
			 */
			public String markupForDisplay (String operator, String operand, NodeFormatting using)
			{ return etaRender (using) + using.formatParenthetical (operand); }

			/* (non-Javadoc)
			 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#configure(java.lang.String)
			 */
			public void configure (String parameters)
			{
				this.configuration = parameters;
			}
			String configuration = null;
			
			/* (non-Javadoc)
			 * @see net.myorb.math.anaconda.ComplexAnacondaRPC.VectorEnabledFunction#functionRpcText()
			 */
			String functionRpcText () { return etaText (configuration); }
		};
	}
	
	/**
	 * @param s function parameter
	 * @param a function denominator value
	 * @return computed value
	 */
	ComplexMarker eta (ComplexValue<Double> s, ComplexValue<Double> a)
	{
		return PythonComplexFunctionRPC.requestEvalFor
		(
			getAnacondaAccess (), etaText (a.Re ().toString ()), s
		);
	}
	
	/**
	 * @param s function parameter
	 * @return computed value
	 */
	ComplexMarker eta (ComplexValue<Double> s)
	{
		return PythonComplexFunctionRPC.requestEvalFor
		(
			getAnacondaAccess (), "mpmath.eta", s
		);
	}

	/**
	 * @param a function denominator value
	 * @return text of the eta function reference
	 */
	String etaText (String a)
	{
		return a == null ? "mpmath.eta" :
		"lambda x: mpmath.eta ( x, " + a + " )";
	}

	/**
	 * @param using the node formatting object used by the render agent
	 * @return formatted reference
	 */
	String etaRender (NodeFormatting using)
	{
		return formatIdentifierFor ("eta", using);
	}


	/*
	 * 		Zeta
	 */

		/* (non-Javadoc)
		 * @see net.myorb.math.anaconda.AnacondaPrimitives#getZetaImplementation()
		 */
		public CommonVectoredFunctionImplementation getZetaImplementation ()
		{
			return new VectorEnabledFunction ()
			{
				@SuppressWarnings("unchecked")
				public ComplexValue<Double> evaluate (List<ComplexValue<Double>> using)
				{
					ComplexMarker value;
					int n = using.size ();
					profile.parameterCheck (n);
					value = n==1? zeta (using.get (0)) : zeta (using.get (0), using.get (1));
					return (ComplexValue<Double>) value;
				}
				FunctionProfile profile = new FunctionProfile ("Zeta", 1, 2);

				/* (non-Javadoc)
				 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#markupForDisplay(java.lang.String, java.lang.String, net.myorb.math.expressions.gui.rendering.NodeFormatting)
				 */
				public String markupForDisplay (String operator, String operand, NodeFormatting using)
				{ return zetaRender (using) + using.formatParenthetical (operand); }

				/* (non-Javadoc)
				 * @see net.myorb.math.expressions.algorithms.CommonOperatorLibrary.CommonFunctionImplementation#configure(java.lang.String)
				 */
				public void configure (String parameters)
				{
					this.configuration = parameters;
				}
				String configuration = null;
				
				/* (non-Javadoc)
				 * @see net.myorb.math.anaconda.ComplexAnacondaRPC.VectorEnabledFunction#functionRpcText()
				 */
				String functionRpcText () { return zetaText (configuration); }
			};
		}
		
		/**
		 * @param s function parameter
		 * @param a function denominator value
		 * @return computed value
		 */
		ComplexMarker zeta (ComplexValue<Double> s, ComplexValue<Double> a)
		{
			return PythonComplexFunctionRPC.requestEvalFor
			(
				getAnacondaAccess (), zetaText (a.Re ().toString ()), s
			);
		}
		
		/**
		 * @param s function parameter
		 * @return computed value
		 */
		ComplexMarker zeta (ComplexValue<Double> s)
		{
			return PythonComplexFunctionRPC.requestEvalFor
			(
				getAnacondaAccess (), "mpmath.zeta", s
			);
		}

		/**
		 * @param a function denominator value
		 * @return text of the zeta function reference
		 */
		String zetaText (String a)
		{
			return a == null ? "mpmath.zeta" :
			"lambda x: mpmath.zeta ( x, " + a + " )";
		}

		/**
		 * @param using the node formatting object used by the render agent
		 * @return formatted reference
		 */
		String zetaRender (NodeFormatting using)
		{
			return formatIdentifierFor ("zeta", using);
		}


}

