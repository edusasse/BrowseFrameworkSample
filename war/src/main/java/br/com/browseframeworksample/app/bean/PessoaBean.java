package br.com.browseframeworksample.app.bean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import br.com.browseframeworksample.domain.Pessoa;
import br.com.browseframeworksample.facade.PessoaFacade;
import br.com.browseframework.base.data.Filter;
import br.com.browseframework.base.data.enums.Restriction;
import br.com.browseframework.base.data.type.CriterionType;
import br.com.browseframework.base.data.type.DataPageType;
import br.com.browseframework.base.data.type.FilterType;

@ManagedBean(name = "pessoaBean")
@ViewScoped
public class PessoaBean {
    // Acesso ao Facade do classificacao
    @ManagedProperty(name = "pessoaFacade", value = "#{pessoaFacade}")
	private PessoaFacade pessoaFacade;
    
    public PessoaBean() {
		
	}
    
    /**
     * AutoComplete para campo descrição
     * @param filtro
     * @return
     */
    public List<Pessoa> doAutoComplete(String filtro){
		List<Pessoa> retorno = null;
		
		// Obtem o campo descricao
		final String campoDesc = "nome";
		if (campoDesc != null){
			// Caso tenha algum valor informado
			if (filtro != null){
				// Força o like como sufixo
				filtro = filtro + "%";
				// Converte o * para um filtro like
				filtro = filtro.replace("*", "%");
			}
			
			final FilterType f1 = new Filter();
			f1.setPropertyName("nome");
			f1.setPropertyValue(filtro);
			f1.setRestriction(Restriction.LIKE);
			
			// Realiza a pesquisa pelo nome
			final DataPageType<Pessoa> dp = getPessoaFacade().findAll(null, new CriterionType[]{f1});
			
			retorno = dp.getData();
		}
		
		return retorno;
	}

    // GETTERS && SETTERS
    
	public PessoaFacade getPessoaFacade() {
		return pessoaFacade;
	}

	public void setPessoaFacade(PessoaFacade pessoaFacade) {
		this.pessoaFacade = pessoaFacade;
	}
	
}