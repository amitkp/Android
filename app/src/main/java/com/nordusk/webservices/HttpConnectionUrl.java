package com.nordusk.webservices;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.nordusk.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;

public class HttpConnectionUrl {

	public static String RESPONSECODE_SUCCESS = "200";
	public static String RESPONSECODE_ERROR = "FAILURE";
	public static String RESPONSECODE_INVALIDCREDENTIAL = "202";
	public static String RESPONSECODE_PHNO_NOT_REGISTERED = "204";
	public static String RESPONSECODE_MANDETORY_PARAMS_MISSING = "205";
	public static String RESPONSECODE_INVALIDAPIKEY = "406";
	public static String RESPONSECODE_APIKEYMISSING = "405";
	public static String RESPONSECODE_SERVERERROR = "500";
	public static String RESPONSECODE_SESSIONTOKENMISSING = "403";
	public static String RESPONSECODE_INVALIDSESSION = "401";
	public static String RESPONSECODE_REQUESTSUCCESS = "900";
	public static String RESPONSECODE_CONNECTIONTIMEOUT = "9001";
	public static String RESPONSECODE_SOCKETTIMEOUT = "903";
	// private static int socket_timeout = 15000;
	// private static int connection_timeout = 20000;

	private static int socket_timeout = 150000;
	private static int connection_timeout = 200000;

	/**
	 * @param mContext
	 * @return {@link String}
	 */
	public static String getBaseURL(Context mContext) {
		return mContext.getResources().getString(R.string.base_url);
	}

	/**
	 * @param mContext
	 * @param type
	 * @return {@link String}
	 */
	public static String getFinalURL(Context mContext, String type) {
		return getBaseURL(mContext) + type;
	}

	/**
	 * @param jsObj
	 * @param key
	 * @return {@link String}
	 */
	public static String getJSONKeyvalue(JSONObject jsObj, String key) {
		String value = "";
		try {
			if (jsObj.has(key)) {
				if (jsObj.getString(key) != null && jsObj.getString(key).length() > 0 && !jsObj.getString(key).equalsIgnoreCase("null"))
					value = jsObj.getString(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * @param context
	 * @return {@link Boolean}
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean outcome = false;

		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo[] networkInfos = cm.getAllNetworkInfo();

			for (NetworkInfo tempNetworkInfo : networkInfos) {

				/**
				 * Can also check if the user is in roaming
				 */
				if (tempNetworkInfo != null && tempNetworkInfo.isConnected()) {
					outcome = true;
					break;
				}
			}
		}

		return outcome;
	}

	/**
	 * Http Common Server error message.
	 */

	public static void serverErrorMessage(Context context, String responsecode) {
		try {
			if (responsecode.equalsIgnoreCase(HttpConnectionUrl.RESPONSECODE_ERROR)) {
				//CustomToast.showToast(context, context.getResources().getString(R.string.error), Toast.LENGTH_SHORT);
			}
//			else if (responsecode.equalsIgnoreCase(HttpConnectionUrl.RESPONSECODE_INVALIDAPIKEY)) {
//				CustomToast.showToast(context, context.getResources().getString(R.string.invalid_api_key), Toast.LENGTH_SHORT);
//			} else if (responsecode.equalsIgnoreCase(HttpConnectionUrl.RESPONSECODE_APIKEYMISSING)) {
//				CustomToast.showToast(context, context.getResources().getString(R.string.api_key_missing), Toast.LENGTH_SHORT);
//			} else if (responsecode.equalsIgnoreCase(HttpConnectionUrl.RESPONSECODE_SERVERERROR)) {
//				CustomToast.showToast(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT);
//			} else {
//				// AndroidUtility.showToast(context,
//				// context.getResources().getString(R.string.server_did_not_respond));
//			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public static String[] post(Context context, String Url, JSONObject jsonObject) throws ConnectTimeoutException {

		String[] result = { "", "" };

		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connection_timeout);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, socket_timeout);
		HttpPost httpPost = null;
		try {



			httpPost = new HttpPost(Url);
//			if (header_list != null && header_list.size() > 0) {
//				for (int i = 0; i < header_list.size(); i++) {
//					NameValuePair nameValuePair = header_list.get(i);
//					httpPost.addHeader(nameValuePair.getName(), nameValuePair.getValue());
//				}
//			}
			


//			if (values != null) {
//				httpPost.setEntity(new UrlEncodedFormEntity(values));
//			}
//			 File httpCacheDir = new File(context.getCacheDir(), "http");
//	          long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
//	          HttpResponseCache.install(httpCacheDir, httpCacheSize);
			
			//httpPost.addHeader("Cache-Control", "cache");
			  //httpPost.setHeader("Cache-Control","max-age=0");

			if(jsonObject!=null && jsonObject.length()>0) {
				Log.e("input_json", jsonObject.toString());
				String obj = "";
				obj = jsonObject.toString();
					httpPost.setHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"));
					StringEntity se = new StringEntity(obj.toString());
					//se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
					httpPost.setEntity(se);
			}

			BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			result[0] = HttpConnectionUrl.RESPONSECODE_REQUESTSUCCESS;
			result[1] = EntityUtils.toString(entity);

			try {
				Log.e("API URL ------>", Url);
				Log.e("API RESPONSE -----> ", result[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			

		} catch (UnsupportedEncodingException e) {
			result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
			e.printStackTrace();
		} catch (ConnectTimeoutException e) {
			result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
			e.printStackTrace();
		} catch (IOException e) {
			result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
			e.printStackTrace();
		} catch (Exception e) {
			result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
			e.printStackTrace();
		}
		return result;
	}

//	public static void generateMPIN(Activity context,Fragment fragment,ItemMpin itemMpin) {
////		String xmlPayloadString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
////				"<ns2:RespListKeys xmlns:ns2=\"http://npci.org/upi/schema/1/0/\">"+
////				"<Head ver=\"1.0\" ts=\"2015-08-26T16:19:35+05:30\" orgId=\"UPI\" msgId=\"e5894968-3f38-44f9-9cdf-f4d42ee51d8a\"/>"+
////				"<Resp reqMsgId=\"1\" result=\"SUCCESS\"/>"+
////				"<keyList><key code=\"\" owner=\"NPCI\" type=\"PKI\" ki=\"KEY0\">"+
////				"<keyValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xsi:type=\"xs:string\">MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAzPA1EH+aCZv/rly4zgRtfgRNr+6Xtp4iTWHEA36WaSia55gyrOAT0UJOtX9nG/NZ77wFzUxhrHuczh3lVO8/ylXh1wpcRsBPLNfg1qXzaU8c7JLk7amD4cV4re1LkqZfOOrri21na9p7Ybw8v9mC/q7xfF3gzySczaq8SG1NCQIDAQAB</keyValue>"+
////				"</key>"+
////				"<key code=\"\" owner=\"NPCI\" type=\"PKI\" ki=\"KEY1\"><keyValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xsi:type=\"xs:string\">MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAzPA1EH+aCZv/rly4zgRtfgRNr+6Xtp4iTWHEA36WaSia55gyrOAT0UJOtX9nG/NZ77wFzUxhrHuczh3lVO8/ylXh1wpcRsBPLNfg1qXzaU8c7JLk7amD4cV4re1LkqZfOOrri21na9p7Ybw8v9mC/q7xfF3gzySczaq8SG1NCQIDAQAB</keyValue>"+
////				"</key></keyList></ns2:RespListKeys>";
//
//		String xmlPayloadString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns2:RespListKeys xmlns:ns2=\"http://npci.org/upi/schema/\"><Head msgId=\"5skv5ptRj1tuJx7Fdtu\" orgId=\"NPCI\" ts=\"2015-12-03T18:27:44+05:30\" ver=\"1.0\"/><Resp reqMsgId=\"d0kSkoT6Iyz3ZFyv\" result=\"SUCCESS\"/><Txn id=\"d0kSkoT6Iyz4l4iA\" ts=\"2015-12-03T18:27:41+05:30\"/><keyList><key code=\"NPCI\" ki=\"20150822\" owner=\"NPCI\" type=\"PKI\"><keyValue xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAzPA1EH+aCZv/rly4zgRtfgRNr+6Xtp4iTWHEA36WaSia55gyrOAT0UJOtX9nG/NZ77wFzUxhrHuczh3lVO8/ylXh1wpcRsBPLNfg1qXzaU8c7JLk7amD4cV4re1LkqZfOOrri21na9p7Ybw8v9mC/q7xfF3gzySczaq8SG1NCQIDAQAB</keyValue></key></keyList><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#dsa-sha1\"/><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><DigestValue>PQCdQmZ90tYmle3IrxGZR41b3BjkfIM5AKFsgN9MOyM=</DigestValue></Reference></SignedInfo><SignatureValue>kQXgtctmIcruItaJA+GVaX+LXvnBxBGvLco1MvtrOPixtiwPM3j6dg==</SignatureValue><KeyInfo><KeyValue><DSAKeyValue><P>6g8wiFx+GatJWs0OmpPWxfJWsukOjion+EKPivYNsgWkaD3qqcJ4nPTctNQcdKhwQD1MraTbGXXJ\n" +
//				"jpae7dk7cQgdNB7LP4pA3a98SawEX4uNjD2YDATcSWbjlu1q2BqWbJisePuLp1eW6yLkoLIhg85w\n" +
//				"Cs/pQ0EHhH9X+aKDJgs=</P><Q>52IxJ67RBiwmB86ln2dAQqZVGG0=</Q><G>4+XXoIoE48eSt92I2YCPjO0RgB9WaftnLtnehrEYBkdPojkyOzbFEnmS95T9AGuSe8ub6n9KyWlQ\n" +
//				"bS93Fq4eF+CEYXepBMjz1KiZXXw5TgGCXCo4Bv/x8v6fQPYCnAYcavbaZAvaj6Pmc07QdwxI45gl\n" +
//				"SYsEntK+YWRdKoEah6Y=</G><Y>HnpNRxcgEWc4MwxsHlor0aQr/yEXJljV/jl3bR5Kiz4GaUxAfxPiutNLUIIodB2/N15HMXpIfmwB\n" +
//				"dME6hF18sgexAfeysiqnaGM3iku+s9s7ce33HmpSSIIPhXapJJld4Yv0p0Adjhl0S2at4Kldo4Ne\n" +
//				"VBM+x1/Q5KbMJDpXV+s=</Y></DSAKeyValue></KeyValue></KeyInfo></Signature></ns2:RespListKeys>";
//		Log.i("xml_payload",xmlPayloadString);
//
//		Intent intent = new Intent(context, PinActivityComponent.class);
//
//		intent.putExtra("keyCode", "");
//
//		intent.putExtra("keyXmlPayload", xmlPayloadString);  // It will get the data from list keys API response
//
//
//		try {
//
//			// This will set the configuration of the app.
//			JSONObject configuration = new JSONObject();
//			configuration.put("pspName", "National Payments Corporation of India (NPCI)");
//			configuration.put("pspMessage", "Your Transaction Id : "+itemMpin.getTransactionId()+" and you will be charged Rs. "+ itemMpin.getAmount());
//			configuration.put("backgroundColor","#FFFFFF");
//			configuration.put("buttonText", "Accept & Continue");
//			//Setting configuration for the app to the intent.
//			Log.i("configuration",configuration.toString());
//			intent.putExtra("configuration", configuration.toString());
//
//
//
//			JSONObject salt = new JSONObject();
//			salt.put("txnId", itemMpin.getTransactionId());
//			salt.put("txnAmount", itemMpin.getAmount());
//			Log.i("salt", salt.toString());
//			intent.putExtra("salt", salt.toString());
//
//
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//
//
//
//		JSONArray controlsArray=new JSONArray();
//
//		JSONArray payInfoArray=new JSONArray();
//
//		try {
//
////            if(spinner.getSelectedItem().toString().contains("OTP")) {
////                /*** Creating OTP Control Schema ***/
////                JSONObject otpControl = new JSONObject();
////                otpControl.put("name", "otp");
////                otpControl.put("label", "Enter your OTP");
////                otpControl.put("type", "password");
////                otpControl.put("keypad", "normal");
////                otpControl.put("validation", "a:4:8");
////                controlsArray.put(otpControl);
////            }
//
//			// if(spinner.getSelectedItem().toString().contains("MPIN")) {
//			/*** Creating mPin Control Schema ***/
//			JSONObject mpinControl = new JSONObject();
//			mpinControl.put("name", "mpin");
//			mpinControl.put("label", "Enter your MPIN");
//			mpinControl.put("type", "password");
//			mpinControl.put("keypad", "normal");
//			mpinControl.put("validation", "an:0:6");
//			controlsArray.put(mpinControl);
//			// }
//
////            if(spinner.getSelectedItem().toString().contains("CardLast4Digit")) {
////
////                /*** Creating ccLast4Digit Control Schema ***/
////
////                JSONObject ccLast4Digit = new JSONObject();
////                ccLast4Digit.put("name", "last4digit");
////                ccLast4Digit.put("label", "Enter your Credit/Debit Card last 4 digit");
////                ccLast4Digit.put("type", "text");
////                ccLast4Digit.put("keypad", "numeric");
////                ccLast4Digit.put("validation", "ans:4:4");
////                controlsArray.put(ccLast4Digit);
////            }
//
//
//
//			/*** Creating Amount Lable Schema ***/
//			JSONObject jsonAmount = new JSONObject();
//			jsonAmount.put("name", "amount");
//			jsonAmount.put("title", "Transaction Amount");
//			jsonAmount.put("value", itemMpin.getAmount());
//			payInfoArray.put(jsonAmount);
//
//			/*** Creating PayeeName Lable Schema ***/
//			JSONObject jsonPayeeName = new JSONObject();
//			jsonPayeeName.put("name", "payeeName");
//			jsonPayeeName.put("title", "Payee Name");
//			jsonPayeeName.put("value", "Neeloy Ghosh");
//			payInfoArray.put(jsonPayeeName);
//
//			/*** Creating txnDesc Lable Schema ***/
//			JSONObject jsonTxnDesc = new JSONObject();
//			jsonTxnDesc.put("name", "txnDesc");
//			jsonTxnDesc.put("title", "Transaction Description");
//			jsonTxnDesc.put("value", "Pay for collect");
//			payInfoArray.put(jsonTxnDesc);
//
//			/*** Creating Amount Lable Schema ***/
//			JSONObject jsonTxnRef = new JSONObject();
//			jsonTxnRef.put("name", "txnRef");
//			jsonTxnRef.put("title", "Transaction Ref.");
//			jsonTxnRef.put("value", itemMpin.getTransactionId());
//			payInfoArray.put(jsonTxnRef);
//
//			Log.i("payInfo", payInfoArray.toString());
//
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		Log.i("Controls JSON", controlsArray.toString());
//		intent.putExtra("controls", controlsArray.toString());
//		intent.putExtra("payInfo", payInfoArray.toString());
//
//		fragment.startActivityForResult(intent, 1);
//
//		try {
//
//			BusProvider.getInstance().post(new InformationEvent("mpin"));
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//	}

	public static void generateOTP(Activity context,Fragment fragment) {
//		String xmlPayloadString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
//				"<ns2:RespListKeys xmlns:ns2=\"http://npci.org/upi/schema/1/0/\">"+
//				"<Head ver=\"1.0\" ts=\"2015-08-26T16:19:35+05:30\" orgId=\"UPI\" msgId=\"e5894968-3f38-44f9-9cdf-f4d42ee51d8a\"/>"+
//				"<Resp reqMsgId=\"1\" result=\"SUCCESS\"/>"+
//				"<keyList><key code=\"\" owner=\"NPCI\" type=\"PKI\" ki=\"KEY0\">"+
//				"<keyValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xsi:type=\"xs:string\">MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAzPA1EH+aCZv/rly4zgRtfgRNr+6Xtp4iTWHEA36WaSia55gyrOAT0UJOtX9nG/NZ77wFzUxhrHuczh3lVO8/ylXh1wpcRsBPLNfg1qXzaU8c7JLk7amD4cV4re1LkqZfOOrri21na9p7Ybw8v9mC/q7xfF3gzySczaq8SG1NCQIDAQAB</keyValue>"+
//				"</key>"+
//				"<key code=\"\" owner=\"NPCI\" type=\"PKI\" ki=\"KEY1\"><keyValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xsi:type=\"xs:string\">MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAzPA1EH+aCZv/rly4zgRtfgRNr+6Xtp4iTWHEA36WaSia55gyrOAT0UJOtX9nG/NZ77wFzUxhrHuczh3lVO8/ylXh1wpcRsBPLNfg1qXzaU8c7JLk7amD4cV4re1LkqZfOOrri21na9p7Ybw8v9mC/q7xfF3gzySczaq8SG1NCQIDAQAB</keyValue>"+
//				"</key></keyList></ns2:RespListKeys>";

		String xmlPayloadString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns2:RespListKeys xmlns:ns2=\"http://npci.org/upi/schema/\"><Head msgId=\"5skv5ptRj1tuJx7Fdtu\" orgId=\"NPCI\" ts=\"2015-12-03T18:27:44+05:30\" ver=\"1.0\"/><Resp reqMsgId=\"d0kSkoT6Iyz3ZFyv\" result=\"SUCCESS\"/><Txn id=\"d0kSkoT6Iyz4l4iA\" ts=\"2015-12-03T18:27:41+05:30\"/><keyList><key code=\"NPCI\" ki=\"20150822\" owner=\"NPCI\" type=\"PKI\"><keyValue xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAzPA1EH+aCZv/rly4zgRtfgRNr+6Xtp4iTWHEA36WaSia55gyrOAT0UJOtX9nG/NZ77wFzUxhrHuczh3lVO8/ylXh1wpcRsBPLNfg1qXzaU8c7JLk7amD4cV4re1LkqZfOOrri21na9p7Ybw8v9mC/q7xfF3gzySczaq8SG1NCQIDAQAB</keyValue></key></keyList><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#dsa-sha1\"/><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><DigestValue>PQCdQmZ90tYmle3IrxGZR41b3BjkfIM5AKFsgN9MOyM=</DigestValue></Reference></SignedInfo><SignatureValue>kQXgtctmIcruItaJA+GVaX+LXvnBxBGvLco1MvtrOPixtiwPM3j6dg==</SignatureValue><KeyInfo><KeyValue><DSAKeyValue><P>6g8wiFx+GatJWs0OmpPWxfJWsukOjion+EKPivYNsgWkaD3qqcJ4nPTctNQcdKhwQD1MraTbGXXJ\n" +
				"jpae7dk7cQgdNB7LP4pA3a98SawEX4uNjD2YDATcSWbjlu1q2BqWbJisePuLp1eW6yLkoLIhg85w\n" +
				"Cs/pQ0EHhH9X+aKDJgs=</P><Q>52IxJ67RBiwmB86ln2dAQqZVGG0=</Q><G>4+XXoIoE48eSt92I2YCPjO0RgB9WaftnLtnehrEYBkdPojkyOzbFEnmS95T9AGuSe8ub6n9KyWlQ\n" +
				"bS93Fq4eF+CEYXepBMjz1KiZXXw5TgGCXCo4Bv/x8v6fQPYCnAYcavbaZAvaj6Pmc07QdwxI45gl\n" +
				"SYsEntK+YWRdKoEah6Y=</G><Y>HnpNRxcgEWc4MwxsHlor0aQr/yEXJljV/jl3bR5Kiz4GaUxAfxPiutNLUIIodB2/N15HMXpIfmwB\n" +
				"dME6hF18sgexAfeysiqnaGM3iku+s9s7ce33HmpSSIIPhXapJJld4Yv0p0Adjhl0S2at4Kldo4Ne\n" +
				"VBM+x1/Q5KbMJDpXV+s=</Y></DSAKeyValue></KeyValue></KeyInfo></Signature></ns2:RespListKeys>";
		Log.i("xml_payload", xmlPayloadString);

//		Intent intent = new Intent(context, PinActivityComponent.class);
//
//		intent.putExtra("keyCode", "");
//
//		intent.putExtra("keyXmlPayload", xmlPayloadString);  // It will get the data from list keys API response
//
//
//		try {
//
//			// This will set the configuration of the app.
//			JSONObject configuration = new JSONObject();
//			configuration.put("pspName", "National Payments Corporation of India (NPCI)");
//			configuration.put("pspMessage", "");
//			configuration.put("backgroundColor","#FFFFFF");
//			configuration.put("buttonText", "Accept & Continue");
//			intent.putExtra("configuration", configuration.toString());
//
////
////
////			JSONObject salt = new JSONObject();
////			salt.put("txnId", itemMpin.getTransactionId());
////			salt.put("txnAmount", itemMpin.getAmount());
////			Log.i("salt", salt.toString());
////			intent.putExtra("salt", salt.toString());
//
//
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//
//
//
//		JSONArray controlsArray=new JSONArray();
//
//
//		try {
//
//
//			/*** Creating OTP Control Schema ***/
//			JSONObject otpControl = new JSONObject();
//			otpControl.put("name", "otp");
//			otpControl.put("label", "Enter your OTP");
//			otpControl.put("type", "password");
//			otpControl.put("keypad", "normal");
//			otpControl.put("validation", "a:4:8");
//			controlsArray.put(otpControl);
//
//
//			intent.putExtra("controls", controlsArray.toString());
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		fragment.startActivityForResult(intent, 1);

//		try {
//
//			BusProvider.getInstance().post(new InformationEvent("mpin"));
//		}catch (Exception e){
//			e.printStackTrace();
//		}

	}

	// HTTP GET request
//	public static String sendGet_(String url, Context context)
//			throws Exception {
//
//		String[] result = { "", "" };
//		try {
//			HttpClient httpclient = getNewHttpClient(context);
//			//httpclient.setSSLSocketFactory(buildSslSocketFactory());
//
//
//			httpclient.getParams()
//					.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
//							connection_timeout);
//			httpclient.getParams().setParameter(
//					CoreConnectionPNames.SO_TIMEOUT, socket_timeout);
//			HttpResponse response = httpclient.execute(new HttpGet(url));
//			StatusLine statusLine = response.getStatusLine();
//			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
//				ByteArrayOutputStream out = new ByteArrayOutputStream();
//				response.getEntity().writeTo(out);
//				String responseString = out.toString();
//				out.close();
//				result[0] = HttpConnectionUrl.RESPONSECODE_REQUESTSUCCESS;
//				result[1] = responseString;
//			} else {
//				response.getEntity().getContent().close();
//				throw new IOException(statusLine.getReasonPhrase());
//			}
//
//		} catch (UnsupportedEncodingException e) {
//			result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
//			e.printStackTrace();
//		} catch (ConnectTimeoutException e) {
//			result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
//			e.printStackTrace();
//		} catch (IOException e) {
//			result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
//			e.printStackTrace();
//		} catch (Exception e) {
//			result[0] = HttpConnectionUrl.RESPONSECODE_CONNECTIONTIMEOUT;
//			e.printStackTrace();
//		}
//		return result[1];
//
//	}


//	public static HttpClient getNewHttpClient(Context context) {
//		try {
////			KeyStore trustStore = KeyStore.getInstance(KeyStore
////					.getDefaultType());
////			trustStore.load(null, null);
////
////			MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
////			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//			MySSLSocketFactory sf=null;
//			try {
//
//				// Get an instance of the Bouncy Castle KeyStore format
//				KeyStore trusted = KeyStore.getInstance("BKS");//put BKS literal
//				// Get the raw resource, which contains the keystore with
//				// your trusted certificates (root and any intermediate certs)
//				InputStream in =context.getResources().openRawResource(R.raw.oussl);
//				try {
//					// Initialize the keystore with the provided trusted certificates
//					// Also provide the password of the keystore
//					trusted.load(null,null);
//				} finally {
//					in.close();
//				}
//				// Pass the keystore to the SSLSocketFactory. The factory is responsible
//				// for the verification of the server certificate.
//				sf = new MySSLSocketFactory(trusted);
//				// Hostname verification from certificate
//
//				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//			} catch (Exception e) {
//				throw new AssertionError(e);
//			}
//
//
//			HttpParams params = new BasicHttpParams();
//			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//
//
//
//			SchemeRegistry registry = new SchemeRegistry();
//			registry.register(new Scheme("http", PlainSocketFactory
//					.getSocketFactory(), 80));
//			registry.register(new Scheme("https", sf, 443));
//
//			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
//					params, registry);
//
////			DefaultHttpClient defaultHttpClient=new DefaultHttpClient();
////		//	defaultHttpClient.getAuthSchemes().register("ntlm", null);
//			return new DefaultHttpClient(ccm, params);
//		} catch (Exception e) {
//			return new DefaultHttpClient();
//		}
//	}

//	private static SSLSocketFactory buildSslSocketFactory(Context context) {
//		// Add support for self-signed (local) SSL certificates
//		// Based on http://developer.android.com/training/articles/security-ssl.html#UnknownCa
//		try {
//
//			// Load CAs from an InputStream
//			// (could be from a resource or ByteArrayInputStream or ...)
//			CertificateFactory cf = CertificateFactory.getInstance("X.509");
//			// From https://www.washington.edu/itconnect/security/ca/load-der.crt
//			InputStream is = context.getResources().getAssets().openAsset("somefolder/somecertificate.crt");
//			InputStream caInput = new BufferedInputStream(is);
//			Certificate ca;
//			try {
//				ca = cf.generateCertificate(caInput);
//				// System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
//			} finally {
//				caInput.close();
//			}
//
//			// Create a KeyStore containing our trusted CAs
//			String keyStoreType = KeyStore.getDefaultType();
//			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//			keyStore.load(null, null);
//			keyStore.setCertificateEntry("ca", ca);
//
//			// Create a TrustManager that trusts the CAs in our KeyStore
//			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//			tmf.init(keyStore);
//
//			// Create an SSLContext that uses our TrustManager
//			SSLContext ssl_context = SSLContext.getInstance("TLS");
//			ssl_context.init(null, tmf.getTrustManagers(), null);
//			return ssl_context.getSocketFactory();
//
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (KeyStoreException e) {
//			e.printStackTrace();
//		} catch (KeyManagementException e) {
//			e.printStackTrace();
//		} catch (CertificateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//
//	}




}
