// Desabilita o F5 
// -------------------------------
document.onkeydown = function(e) {
   // keycode for F5 function
   if (e.keyCode === 116) {
      return false; 
   }

};

function setBorder(ElementId){
	alert($(ElementId))
	$(ElementId).css("color","red");
}

// Mascara generica em javascript
// -------------------------------
function mascaraTexto(evento, mascara) {  
     
   var campo, valor, i, tam, caracter;  
     
   if (document.all) // Internet Explorer
      campo = evento.srcElement;  
   else // Nestcape, Mozzila
       campo= evento.target;  
         
   valor = campo.value;  
   tam = valor.length;  
     
   for(i=0;i<mascara.length;i++){  
      caracter = mascara.charAt(i);  
      if(caracter!="9")   
         if(i<tam & caracter!=valor.charAt(i))  
            campo.value = valor.substring(0,i) + caracter + valor.substring(i,tam);  
                 
   }  
  
}
 
// Miniminiza a janela
// -------------------------------
function Minimize() {
	window.innerWidth = 100;
	window.innerHeight = 100;
	window.screenX = screen.width;
	window.screenY = screen.height;
	alwaysLowered = true;
}

// Maximiza
// -------------------------------
function Maximize() {
	window.innerWidth = screen.width;
	window.innerHeight = screen.height;
	window.screenX = 0;
	window.screenY = 0;
	alwaysLowered = false;
}

// Submit do form principal
// -------------------------------
function submitForm() {
	document.form.submit();
	return true;
} 

/*******************************************************************************
 * Dynamic Ajax Content- © Dynamic Drive DHTML code library
 * (www.dynamicdrive.com) This notice MUST stay intact for legal use Visit
 * Dynamic Drive at http://www.dynamicdrive.com/ for full source code
 ******************************************************************************/
var loadedobjects=""
var rootdomain="http://"+window.location.hostname

function ajaxpage(url, containerid) {
    var page_request = false
    if (window.XMLHttpRequest) // if Mozilla, Safari etc
    page_request = new XMLHttpRequest()
    else if (window.ActiveXObject) { // if IE
        try {
            page_request = new ActiveXObject("Msxml2.XMLHTTP")
        } catch (e) {
            try {
                page_request = new ActiveXObject("Microsoft.XMLHTTP")
            } catch (e) {}
        }
    } else return false
    page_request.onreadystatechange = function () {
        loadpage(page_request, containerid)
    }
    page_request.open('GET', url, true)
    page_request.send(null)
}

function loadpage(page_request, containerid) {
    if (page_request.readyState == 4 && (page_request.status == 200 || window.location.href.indexOf("http") == -1)) document.getElementById(containerid).innerHTML = page_request.responseText
}

function loadobjs() {
    if (!document.getElementById) return
    for (i = 0; i < arguments.length; i++) {
        var file = arguments[i]
        var fileref = ""
        if (loadedobjects.indexOf(file) == -1) { // Check to see if this
													// object has not
            // already been added to page before
            // proceeding
            if (file.indexOf(".js") != -1) { // If object is a js file
                fileref = document.createElement('script')
                fileref.setAttribute("type", "text/javascript");
                fileref.setAttribute("src", file);
            } else if (file.indexOf(".css") != -1) { // If object is a css
														// file
                fileref = document.createElement("link")
                fileref.setAttribute("rel", "stylesheet");
                fileref.setAttribute("type", "text/css");
                fileref.setAttribute("href", file);
            }
        }
        if (fileref != "") {
            document.getElementsByTagName("head").item(0).appendChild(fileref)
            loadedobjects += file + " " // Remember this object as being already
										// added to page
        }
    }
}
 

// Ação - Clipagem
// -------------------------------
var veiculoComunicacaoIDCorrente = -1;
var valorPosicaoCorrente = 0;
var elementoCorrente = null;
var atualizaValor = true;

function doRecalcularValoresAnuncio()
{
	atualizaValor = false;
	var mudou = false;
	var elemVeiculoComunicacao = document.getElementById(cmbAcaoVeiculoComunicacaoVar.id).children[1];
	var str = elemVeiculoComunicacao.value;
	if (str != null){
		var id=str.split("@")[0];
		if (veiculoComunicacaoIDCorrente != id){
			veiculoComunicacaoIDCorrente = id;
			mudou=true;
		}
	}

	if (mudou && veiculoComunicacaoIDCorrente != '' && veiculoComunicacaoIDCorrente > 0){
		var elem = document.getElementById(txtAcaoValorAnuncioVar.id).firstChild; 
		rcFrmAcaoGetVeiculoComunicacao(veiculoComunicacaoIDCorrente);
	}
}

function doCalcularValorAnuncio(v)
{
	atualizaValor = true;
	
	elementoCorrente = v;
	var mudou = false;
	var elemVeiculoComunicacao = document.getElementById(cmbAcaoVeiculoComunicacaoVar.id).children[1];
	var str = elemVeiculoComunicacao.value;
	if (str != null){
		var id=str.split("@")[0];
		if (veiculoComunicacaoIDCorrente != id){
			veiculoComunicacaoIDCorrente = id;
			mudou=true;
		}
	}
	
	if (mudou){
		rcFrmAcaoGetVeiculoComunicacao(veiculoComunicacaoIDCorrente);
	} else {
		doAtualizaValor();
	}

}

function doAtualizaValor() 
{
	var elem = document.getElementById(txtAcaoValorAnuncioVar.id).firstChild;
	var elemH = document.getElementById(txtAcaoValorAnuncioVar.id).children[1];
	if (elem.value == ''){
		elem.value = 0;
		elemH.value = 0;
	}

	elem.value = elem.value.replace(",",".");
 	if(document.getElementById(elementoCorrente).checked){
 		elem.value = parseFloat(elem.value) + parseFloat(valorPosicaoCorrente);
 		elem.value = Math.round(elem.value * Math.pow(10,5))/Math.pow(10,5);
 	} else {
 		elem.value = parseFloat(elem.value) - parseFloat(valorPosicaoCorrente);
 		elem.value = Math.round(elem.value * Math.pow(10,5))/Math.pow(10,5);
	}
 	if (parseFloat(elem.value) < 0){
 		elem.value = 0;
 	}
 	elemH.value = elem.value;
 	elem.value = elem.value.replace(".",",");
	
}

function handleCompleteGetVeiculoComunicacao(xhr, status, args) 
{
	if (atualizaValor){
		valorPosicaoCorrente = args.valorAnuncio;
		doAtualizaValor();
	} else {
		/* Atualiza somente se o valor retornado mudou */
		if (valorPosicaoCorrente != args.valorAnuncio){
			valorPosicaoCorrente = args.valorAnuncio;
			var elem = document.getElementById(txtAcaoValorAnuncioVar.id).firstChild;
			var elemH = document.getElementById(txtAcaoValorAnuncioVar.id).children[1];
			elem.value = 0;
			elemH.value = 0;
			elem.value = elem.value.replace(",",".");
			$(".anuncio :checked").each(function( index ) {
			 	elem.value = parseFloat(elem.value) + parseFloat(valorPosicaoCorrente);
			 	elem.value = Math.round(elem.value * Math.pow(10,5))/Math.pow(10,5);

			 	if (parseFloat(elem.value) < 0){
			 		elem.value = 0;
			 	}
			 	elemH.value = elem.value;
			 	elem.value = elem.value.replace(".",",");
			 	
			});
		}
	}
}

function atualizaTela(){
	hasAppDialogAberta();
}

function handleCompleteHasAppDialogAberta(xhr, status, args) 
{	
	var hasAppDialogAberta = args.hasAppDialogAberta;
	if (hasAppDialogAberta == false){
		doAtualizarTela();
	}
}