
/*

public JSON client_ajouter(String logiLille1,String soldeini, String IdCarte, String decouvert){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/client/ajouter");
		List<NameValuePair> postParameters = new ArrayList>NameValuePair>();
		postParameters.add(new BasicNameValuePair("loginLille1", loginLille1));
		postParameters.add(new BasicNameValuePair("soldeInitial",soldeini));
		postParameters.add(new BasicNameValuePair("idCarte", IdCarte));
		postParameters.add(new BasicNameValuePair("decouvertAutorise",decouvert));		
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse responce = httpclient.execute(httppost);
		return(responce);
	} 
	catch(Exception e) {}
}


public void client_carte(String logiLille1,String newIdCarte){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/client/carte");
		List<NameValuePair> postParameters = new ArrayList>NameValuePair>();
		postParameters.add(new BasicNameValuePair("loginLille1", loginLille1));
		postParameters.add(new BasicNameValuePair("idCarte",newIdCarte));		
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse responce = httpclient.execute(httppost);
	} 
	catch(Exception e) {}
}

public JSON client_liste(){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/client/liste");

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse responce = httpclient.execute(httppost);
		return(responce);
	} 
	catch(Exception e) {}
}


public void client_decouvert(String idCarte,String newDecouvertAutorise){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/client/decouvert");
		List<NameValuePair> postParameters = new ArrayList>NameValuePair>();
		postParameters.add(new BasicNameValuePair("idCarte",idCarte));
		postParameters.add(new BasicNameValuePair("decouvertAutorise", newDecouvertAutorise));		
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.execute(httppost);
	} 
	catch(Exception e) {}
}


public JSON utilisateur_connexion(String login, String mdp, String idCarte, String loginLille1){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/utilisateur/connexion");
		List<NameValuePair> postParameters = new ArrayList>NameValuePair>();
		postParameters.add(new BasicNameValuePair("login",idCarte));
		postParameters.add(new BasicNameValuePair("mdp",mdp));
		postParameters.add(new BasicNameValuePair("idCarte",idCarte));
		postParameters.add(new BasicNameValuePair("loginLille1", loginLille1));		
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse responce = httpclient.execute(httppost);
		return(responce);
	} 
	catch(Exception e) {}
}

public void utilisateur_deconnexion(){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/utilisateur/deconnexion");

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.execute(httppost);
	} 
	catch(Exception e) {}
}


public void utilisateur_mdp(String login, String newmdp){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/utilisateur/mdp");
		List<NameValuePair> postParameters = new ArrayList>NameValuePair>();
		postParameters.add(new BasicNameValuePair("login",idCarte));
		postParameters.add(new BasicNameValuePair("mdp",newmdp));	
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.execute(httppost);
	} 
	catch(Exception e) {}
}


public void utilisateur_droit(String login, String newdroit){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/utilisateur/droit");
		List<NameValuePair> postParameters = new ArrayList>NameValuePair>();
		postParameters.add(new BasicNameValuePair("login",idCarte));
		postParameters.add(new BasicNameValuePair("niveau de droit",newdroit));	
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.execute(httppost);
	} 
	catch(Exception e) {}
}

public void utilisateur_ajouter(String login, String mdp, String loginLille1, String idCarte, String droit){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/utilisateur/ajouter");
		List<NameValuePair> postParameters = new ArrayList>NameValuePair>();
		postParameters.add(new BasicNameValuePair("login",idCarte));
		postParameters.add(new BasicNameValuePair("mdp",mdp));
	        postParameters.add(new BasicNameValuePair("loginLille1", loginLille1));
		postParameters.add(new BasicNameValuePair("idCarte",IdCarte));
	   	postParameters.add(new BasicNameValuePair("niveau de droit",droit));	
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.execute(httppost);
	} 
	catch(Exception e) {}
}



public void utilisateur_carte(String login, String loginLille1, String idCarte){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/utilisateur/carte");
		List<NameValuePair> postParameters = new ArrayList>NameValuePair>();
		postParameters.add(new BasicNameValuePair("login",idCarte));
	        postParameters.add(new BasicNameValuePair("loginLille1", loginLille1));
		postParameters.add(new BasicNameValuePair("idCarte",newIdCarte));
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.execute(httppost);
	} 
	catch(Exception e) {}
}


public void utilisateur_supprimer(String login){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/utilisateur/carte");
		List<NameValuePair> postParameters = new ArrayList>NameValuePair>();
		postParameters.add(new BasicNameValuePair("login",idCarte));
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.execute(httppost);
	} 
	catch(Exception e) {}
}


public JSON transaction_rechargement(String idCarte, String montant){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/transaction/rechargement");
		List<NameValuePair> postParameters = new ArrayList>NameValuePair>();
		postParameters.add(new BasicNameValuePair("idCarte",idCarte));
		postParameters.add(new BasicNameValuePair("montant",montant));		
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse responce = httpclient.execute(httppost);
		return(responce);
	} 
	catch(Exception e) {}
}


public JSON transaction_paiementt(String idCarte, String montant, String nbConso){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/transaction/paiement");
		List<NameValuePair> postParameters = new ArrayList>NameValuePair>();
		postParameters.add(new BasicNameValuePair("idCarte",idCarte));
		postParameters.add(new BasicNameValuePair("montant",montant));
		postParameters.add(new BasicNameValuePair("qte",nbConso));	
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse responce = httpclient.execute(httppost);
		return(responce);
	} 
	catch(Exception e) {}
}



public JSON transaction_vidange(String idCarte){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/transaction/vidange");
		List<NameValuePair> postParameters = new ArrayList>NameValuePair>();
		postParameters.add(new BasicNameValuePair("idCarte",idCarte));	
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse responce = httpclient.execute(httppost);
		return(responce);
	} 
	catch(Exception e) {}
}


public void transaction_annuler(String idTransac){
	try{
		Httppost httpost = new HttpPost("10p5.clubinfo.frogeye.fr/api/transaction/annuler");
		List<NameValuePair> postParameters = new ArrayList>NameValuePair>();
		postParameters.add(new BasicNameValuePair("id",idTransac));	
		httppost.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.execute(httppost);
	} 
	catch(Exception e) {}
}
*/