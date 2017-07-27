package fr.bludwarf.commons.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class TestUtils
{
	private static final String MAP_DELIM = " = ";
	protected static Logger LOG = Logger.getLogger(TestUtils.class);

	public static <K,V> String mapToString(Map<K, V> map)
	{
		final StringBuilder sb = new StringBuilder();

		sb.append('{').append('\n');

		for (final K key : map.keySet())
		{
			sb.append('\t').append(key.toString()).append(MAP_DELIM).append(map.get(key).toString()).append('\n');
		}
		
		sb.append('}');
		
		return sb.toString();
	}

	public static void info(String msg)
	{
		JOptionPane.showMessageDialog(null, msg);
	}

	public static void confirm(String msg)
	{
		assertEquals("L'utilisateur n'a pas répondu oui à : " + msg, 0, JOptionPane.showConfirmDialog(null, msg));
	}

	/**
	 * Se base sur une indentation 
	 * @param expected
	 * @param actual
	 * @param removeActualOnSuccess
	 * @throws IOException
	 */
	public static void assertEqualsXML(final File expected, final File actual, final boolean removeActualOnSuccess) throws IOException
	{
		final String expectedContent = FileUtils.readFileToString(expected);
		String actualContent   = FileUtils.readFileToString(actual);
		
		// Normalisation des indentation
		// On prend modèle sur le premier fichier (expected)
		actualContent = formatXMLFromModel(actualContent, expectedContent);
		
		assertEquals(expectedContent, actualContent);
		assertTrue("Le test a réussi mais il est impossible de supprimer le fichier généré", actual.delete());
	}

	/**
	 * Formatte les retour à la ligne et les caractères d'indentation d'un contenu XML en fonction d'un modèle
	 * @return le contenu XML formatté en fonction d'un modèle (expected)
	 */
	protected static String formatXMLFromModel(final String xml, final String model)
	{
		String expBR = null;
		String expIndent = null;
		String actBR = null;
		String actIndent = null;
		
		String actual = xml;
		
		// On prend modèle sur le premier fichier (expected)
		
		// TODO : constant
		final Pattern p = Pattern.compile("(\\r?\\n)(\\s*)<");
		Matcher m = p.matcher(removeXmlProlog(model));
		if (m.find())
		{
			expBR = m.group(1);
			expIndent = m.group(2);
		}
		
		m = p.matcher(removeXmlProlog(xml));
		if (m.find())
		{
			actBR = m.group(1);
			actIndent = m.group(2);
		}
		
		// BR
		if (expBR != null && actBR != null)
		{
			actual = actual.replace(actBR, expBR);
		}
		
		// Indent
		if (expIndent != null && actIndent != null)
		{
			actual = actual.replace(actIndent, expIndent);
		}
		
		return actual;
	}

	/**
	 * @param model
	 * @return
	 */
	private static String removeXmlProlog(final String model)
	{
		return model.trim().replaceFirst("<\\?xml.+>(\\r?\\n)", "");
	}
}
