package jfan.io.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import jfan.io.json.annotations.JsonTransient;



/**
 * Classe que serializa objetos em JSON. 
 * Classe simples somente par dar suporte ao JFAN, portanto não trata referências cíclicas e outras coisas
 * geralmente tratadas em bibliotecas especializadas. 
 * @author Filipe Pais Lenfers
 *
 */
public class SerializadorJson {
	
	StringBuilder sb;

	private void adicionarValor(String chave,Object o) {
		sb.append("\"");
        sb.append(chave);
        sb.append( "\":" );
        adicionarValor(o,false);
     }
	
	public String serializar(Object o) {
		sb = new StringBuilder();
		//adicionarObjeto(o, false); //Esse era o q funcionava pra todos filipe
		adicionarValor(o, false);
		String s = sb.toString();
		sb = null;
		return s;
		
	}
	
	@SuppressWarnings("unchecked")
	private void adicionarObjeto(Object o, boolean adicionarVirgula) {
		if (o == null) return;
		try {
			
			Class classePrincipal = o.getClass(); 
			
			Field[] campos =  classePrincipal.getDeclaredFields();
			Object temp;
			
			
			
			sb.append("{");
						
			sb.append("class:\"");
			sb.append(classePrincipal.getCanonicalName());
			sb.append("\",");
			
			int modifiers;
			
			for(Field campo : campos) {
								
				campo.setAccessible(true);
						
				modifiers = campo.getModifiers();
				
				
				Class superClasse = classePrincipal.getSuperclass();
				
				//TODO hoje só trata 1 de profundidade, tratar indeterminadamente.
				JsonTransient jsonTransient = null;
				try {
					jsonTransient = superClasse.getDeclaredField(campo.getName()).getAnnotation(JsonTransient.class);
				}
				catch (NoSuchFieldException e) {
					//throw new RuntimeException(e);
				}
							
				if(!(Modifier.isTransient(modifiers)||Modifier.isStatic(modifiers)|| jsonTransient != null ))
				{
					temp = campo.get(o);
					adicionarValor(campo.getName(), temp);
					sb.append(",");
				}
			}
			
			sb.deleteCharAt(sb.length()-1);
			sb.append("}");
			
			if (adicionarVirgula) {
				sb.append(",");
			}
			
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} 
	}
	
	private void adicionarValor(Object o, boolean adicionarVirgula) {
		if (o == null) { //é nulo
	   	 sb.append("null");
	    }
	    else if (o instanceof Boolean) { //é um booleano
	   	 Boolean b = (Boolean) o;
	   	 sb.append(b ? "true" : "false");
	    }
	    else if (o instanceof Number) { //é um número 
	        sb.append(o.toString());
	    }
	    else if (o instanceof Character || o instanceof String) { //é um texto
	   	 sb.append("\"");
	        sb.append((String)o);
	        sb.append("\"");
	    }
	    else if (o.getClass().isArray()) { // é um array
	        adicionarArray(o);
	    }
	    else { //Senão é um objeto
	        adicionarObjeto(o,adicionarVirgula);
	    }
	}
	

	private void adicionarArray(Object o) {
		
		
		
		//Se o último caratere for um ] e o agora eu vo adicionar um array de novo então adiciona a ,
		if (sb.length() > 0 && sb.charAt(sb.length()-1) == ']') { //caso seja uma matriz
			sb.append(",");
		}

		//Adiciona o array
        sb.append("["); 
        int length = Array.getLength(o);
        for (int i = 0; i < length; ++i) { 
            
            adicionarValor(Array.get(o, i),false);
           	sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1); //Remove a última vírgula
        sb.append("]"); 
    }

	

	
}
