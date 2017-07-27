package fr.bludwarf.commons.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.apache.commons.configuration.PropertiesConfiguration;

public class JUnitTest
{
	
	public interface Code
	{
		public void run() throws Exception;
	}
	
	/**
	 * Exemple d'utilisation :
	 * <pre>
	 * JUnitTest.assertException(new Runnable() {
	 * 	\@Override
	 * 	public void run()
	 * 	{
	 * 		rep.get(nom);
	 * 	}
	 * }, PlusieursFilmsPossiblesException.class);
	 * </pre>
	 * @param code
	 * @param clazz
	 */
	public static <T>  void assertException(final Code code, final Class<?> clazz)
	{
		try
		{
			code.run();
			fail("Devrait déclencher une exception " + clazz.getSimpleName());
		}
		catch (final Exception e)
		{
			assertEquals(clazz, e.getClass());
		}
	}
	
	/**
	 * Exemple d'utilisation :
	 * <pre>
	 * final PropertiesConfiguration testProps = new PropertiesConfiguration();
	 * testProps.setProperty("data.dispos", "src/test/resources/planning/2012-05-31/dispos.xml");
	 * 
	 * JUnitTest.testAvecProperties(new Code() {
	 * 	
	 * 	&#64;Override
	 * 	public void run() throws Exception
	 * 	{
	 * 		// Guéna est ponctuellement dispo le 30/05/2012
	 * 		DispoRepository.getInstance();
	 * 		final Benevole guena = BenevoleRepository.getInstance().get("Guéna");
	 * 		
	 * 		final Projection proj = new Projection(DateUtils.parse("31/05/2012 20:30"));
	 * 		proj.setFilm(FILM);
	 * 		assertTrue(guena.estDispo(proj));
	 * 		
	 * 		final Projection proj2 = new Projection(DateUtils.parse("01/06/2012 20:30"));
	 * 		proj2.setFilm(FILM);
	 * 		assertFalse(guena.estDispo(proj2));
	 * 	}
	 * }, testProps, Properties.getInstance());
	 * </pre>
	 * @param code
	 * @param testProps
	 * @param props
	 * @throws Exception
	 */
	public static void testAvecProperties(Code code, final PropertiesConfiguration testProps, final PropertiesConfiguration props) throws Exception
	{
		// Sauvegarde
		PropertiesConfiguration oldProps = new PropertiesConfiguration();
		final Iterator<String> modifiedKeys = testProps.getKeys();
		while (modifiedKeys.hasNext())
		{
			final String key = modifiedKeys.next();
			final Object old = props.getProperty(key);
			final Object niou = testProps.getProperty(key);
			oldProps.setProperty(key, old);
			props.setProperty(key, niou);
		}
		
		try
		{
			code.run();
		}
		
		finally
		{
			// Reload
			while (modifiedKeys.hasNext())
			{
				final String key = modifiedKeys.next();
				final Object old = oldProps.getProperty(key);
				props.setProperty(key, old);
			}
		}
	}
}
