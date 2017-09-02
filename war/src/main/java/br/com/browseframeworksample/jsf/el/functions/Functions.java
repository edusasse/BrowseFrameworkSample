package br.com.browseframeworksample.jsf.el.functions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.Column;
import javax.persistence.JoinColumn;

import br.com.browseframeworksample.app.Aplicacao;
import br.com.browseframework.base.data.dto.BaseDTO;
import br.com.browseframework.jsfprimefaces.util.FacesUtil;
import br.com.browseframework.util.reflection.ReflectionUtil;

public final class Functions {

	private Functions() {
	}

	/**
	 * Concatena as duas strings informadas separando-as pelo separador. Caso
	 * uma das string seja nula ou este em branco apenas uma delas é exibida,
	 * ignorando também o separado.
	 * 
	 * @param s1
	 * @param s2
	 * @param separator
	 * @return
	 */
	public static String concat(String s1, String s2, String separator) {
		String retorno = "";

		if (s1!=null && s1.trim().length() == 0){
			s1 = null;
		}
		if (s2!=null && s2.trim().length() == 0){
			s2 = null;
		}
		
		if (s1 == null && s2 != null) {
			retorno = s2;
		} else if (s2 == null && s1 != null) {
			retorno = s1;
		} else if (s1 != null && s2 != null) {
			retorno = s1.trim() + separator + s2.trim();
		}
		return retorno;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List convertEnumToList(String o) {
		final List result = new ArrayList();
		if (o != null){
			Class clazzEnum = null;
			try {
				clazzEnum = Class.forName(o);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if (clazzEnum != null && clazzEnum.isEnum()){
				result.addAll(EnumSet.allOf(clazzEnum));
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public static List<SelectItem> convertEnumToSelectItemList(String o) {
		final List<SelectItem> result = new ArrayList<SelectItem>();
		List list = convertEnumToList(o);
		for (Object obj :list){
			result.add(new SelectItem(obj));
		}
		return result;
	}
	/**
	 * Cria a lista de SelectItems.
	 * @param aplicacao
	 * @param field
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })

	public static List<SelectItem> getListFindAllFromFacade(Aplicacao aplicacao, String field) {
		final List<SelectItem> retorno = new ArrayList<SelectItem>();
		
		if (aplicacao != null){
			final List aux = aplicacao.getFacade().findAll(null).getData();
			for (Object o : aux){
				retorno.add(new SelectItem(o, String.valueOf(ReflectionUtil.getObjectValue(o, field))));
			}
		}
		
		return retorno;
	}
	
	public static String getHibernateColmumnInfo(String fieldPath, String propertyName) {
		String result = null;
		
		final String[] dto = fieldPath.split("\\.");
		
		String bean = "";
		String property = "";
		
		for (int i = 0; i < dto.length ; i++){
			String ponto = (i != (dto.length-1) ? "." : "" );
			if (i <= 1){
				bean += dto[i]+(i == 0 ? "." : "" );;
			} else {
				property += dto[i] + ponto;
			}
			
		}
		
		@SuppressWarnings("rawtypes")
		final BaseDTO baseDto = (BaseDTO) FacesUtil.resolveExpression("#{"+bean+"}");
		
		if (baseDto != null){
			final Field f = ReflectionUtil.getDeclaredFieldForClass(baseDto.getClass(), property);
			
			final Column c = f.getAnnotation(Column.class);
			if (c != null){
				if ("length".equalsIgnoreCase(propertyName)){
					result = String.valueOf(c.length());
				} else if ("nullable".equalsIgnoreCase(propertyName)){
					result = String.valueOf(c.nullable());
				}
			}
			
			final JoinColumn jc = f.getAnnotation(JoinColumn.class);
			if (jc != null){
				if ("length".equalsIgnoreCase(propertyName)){
					result = String.valueOf(Integer.MAX_VALUE);
				} else if ("nullable".equalsIgnoreCase(propertyName)){
					result = String.valueOf(jc.nullable());
				}
			}
		}
		
		return result;
	}

}