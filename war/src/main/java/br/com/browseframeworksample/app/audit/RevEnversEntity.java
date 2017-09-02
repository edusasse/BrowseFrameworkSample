package br.com.browseframeworksample.app.audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;


@Entity
@Table(name = "auditoria_revisao")
@RevisionEntity(RevEnversListener.class)
public class RevEnversEntity {
	
	@RevisionNumber
    @Id
    @GeneratedValue
    @Column(name="nro_rev")
    private int id;

    @RevisionTimestamp
    @Column(name="nro_timestamp")
    private long timestamp;

	@Column(name = "des_apelido")
	private String apelido;
	
	@Column(name = "des_aplicacao")
	private String aplicacao;

	// GETTERS && SETTERS 
	
	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getAplicacao() {
		return aplicacao;
	}

	public void setAplicacao(String aplicacao) {
		this.aplicacao = aplicacao;
	}
}