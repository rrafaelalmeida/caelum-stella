package br.com.caelum.stella.boleto.bancos;

import java.net.URL;

import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Emissor;

@Deprecated
public class Real extends AbstractBanco implements Banco {

	private final static String NUMERO_REAL = "356";
	private final static String DIGITO_REAL = "5";

	@Override
	public String geraCodigoDeBarrasPara(Boleto boleto) {
		StringBuilder codigoDeBarras = new StringBuilder();
		codigoDeBarras.append(getNumeroFormatado());
		codigoDeBarras.append(String.valueOf(boleto.getCodigoEspecieMoeda()));
		// Digito Verificador sera inserido aqui.

		codigoDeBarras.append(boleto.getFatorVencimento());
		codigoDeBarras.append(boleto.getValorFormatado());

		Emissor emissor = boleto.getEmissor();

		codigoDeBarras.append(emissor.getAgenciaFormatado());
		codigoDeBarras.append(getContaCorrenteDoEmissorFormatado(emissor));

		codigoDeBarras.append(calculaDigitaoDeCobranca(emissor));

		codigoDeBarras.append(getNossoNumeroDoEmissorFormatado(emissor));

		codigoDeBarras.insert(4, this.dvGenerator.geraDigitoMod11(codigoDeBarras.toString()));

		return codigoDeBarras.toString();
	}

	@Override
	public String getNumeroFormatadoComDigito() {
		return NUMERO_REAL + "-" + DIGITO_REAL;
	}

	/**
	 * Calculo relacionado apenas ao Banco Real
	 * 
	 * @param emissor
	 * @return
	 */
	private int calculaDigitaoDeCobranca(Emissor emissor) {
		return this.dvGenerator.geraDigitoMod10(getNossoNumeroDoEmissorFormatado(emissor)
				+ emissor.getAgenciaFormatado() + getContaCorrenteDoEmissorFormatado(emissor));
	}

	@Override
	public URL getImage() {
		return getClass().getResource(String.format("/br/com/caelum/stella/boleto/img/%s.png", getNumeroFormatado()));
	}

	@Override
	public String getNumeroFormatado() {
		return NUMERO_REAL;
	}

	@Override
	public String getCarteiraDoEmissorFormatado(Emissor emissor) {
		return String.format("%02d", emissor.getCarteira());
	}

	@Override
	public String getContaCorrenteDoEmissorFormatado(Emissor emissor) {
		return String.format("%07d", emissor.getContaCorrente());
	}

	@Override
	public String getNossoNumeroDoEmissorFormatado(Emissor emissor) {
		return String.format("%013d", emissor.getNossoNumero());
	}

}
