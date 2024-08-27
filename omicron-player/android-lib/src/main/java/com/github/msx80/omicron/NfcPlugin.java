package com.github.msx80.omicron;

import java.util.function.Consumer;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.util.Log;
import android.widget.Toast;


public class NfcPlugin implements AndroidPlugin {
	
	NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    final static String TAG = "nfc_test";
	
	Consumer<byte[]> onTag = null;
	Context context;
	
	public void init(HardwareInterface hw)
	{
		
		context = (Context)hw;

		try{
			   //Initialise NfcAdapter
			nfcAdapter = NfcAdapter.getDefaultAdapter(context);
			//If no NfcAdapter, display that the device has no NFC 
			if (nfcAdapter == null){
			   Toast.makeText(context,"NO NFC Capabilities",
							  Toast.LENGTH_SHORT).show();
				throw new RuntimeException("NO NFC Capabilities");
			}
			//Create a PendingIntent object so the Android system can 
			//populate it with the details of the tag when it is scanned.
			//PendingIntent.getActivity(Context,requestcode(identifier for 
			//                           intent),intent,int) 
			  pendingIntent = PendingIntent.getActivity(context,0,new Intent(context,context.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
		
			onResume();
		//    ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
		    //Bundle bundle = ai.metaData;
		    //String gameClass = bundle.getString("gameClass");
			
		
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}
		
	}
	
   
   private String detectTagData(Tag tag) {
    StringBuilder sb = new StringBuilder();
    byte[] id = tag.getId();
    sb.append("ID (hex): ").append(toHex(id)).append('\n');
    sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
    sb.append("ID (dec): ").append(toDec(id)).append('\n');
    sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

    String prefix = "android.nfc.tech.";
    sb.append("Technologies: ");
    for (String tech : tag.getTechList()) {
        sb.append(tech.substring(prefix.length()));
        sb.append(", ");
    }

    sb.delete(sb.length() - 2, sb.length());

    for (String tech : tag.getTechList()) {
        if (tech.equals(MifareClassic.class.getName())) {
            sb.append('\n');
            String type = "Unknown";

            try {
                MifareClassic mifareTag = MifareClassic.get(tag);

                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
                sb.append("Mifare Classic type: ");
                sb.append(type);
                sb.append('\n');

                sb.append("Mifare size: ");
                sb.append(mifareTag.getSize() + " bytes");
                sb.append('\n');

                sb.append("Mifare sectors: ");
                sb.append(mifareTag.getSectorCount());
                sb.append('\n');

                sb.append("Mifare blocks: ");
                sb.append(mifareTag.getBlockCount());
            } catch (Exception e) {
                sb.append("Mifare classic error: " + e.getMessage());
            }
        }

        if (tech.equals(MifareUltralight.class.getName())) {
            sb.append('\n');
            MifareUltralight mifareUlTag = MifareUltralight.get(tag);
            String type = "Unknown";
            switch (mifareUlTag.getType()) {
                case MifareUltralight.TYPE_ULTRALIGHT:
                    type = "Ultralight";
                    break;
                case MifareUltralight.TYPE_ULTRALIGHT_C:
                    type = "Ultralight C";
                    break;
            }
            sb.append("Mifare Ultralight type: ");
            sb.append(type);
        }
    }
    Log.v("test",sb.toString());
    return sb.toString();
}
private String toHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (int i = bytes.length - 1; i >= 0; --i) {
        int b = bytes[i] & 0xff;
        if (b < 0x10)
            sb.append('0');
        sb.append(Integer.toHexString(b));
        if (i > 0) {
            sb.append(" ");
        }
    }
     return sb.toString();
}

private String toReversedHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < bytes.length; ++i) {
        if (i > 0) {
            sb.append(" ");
        }
        int b = bytes[i] & 0xff;
        if (b < 0x10)
            sb.append('0');
        sb.append(Integer.toHexString(b));
    }
    return sb.toString();
}

private long toDec(byte[] bytes) {
    long result = 0;
    long factor = 1;
    for (int i = 0; i < bytes.length; ++i) {
        long value = bytes[i] & 0xffl;
        result += value * factor;
        factor *= 256l;
    }
    return result;
}

private long toReversedDec(byte[] bytes) {
    long result = 0;
    long factor = 1;
    for (int i = bytes.length - 1; i >= 0; --i) {
        long value = bytes[i] & 0xffl;
        result += value * factor;
        factor *= 256l;
    }
    return result;
}
   
   
   @Override
public void onResume() {
    nfcAdapter.enableForegroundDispatch((Activity)context,pendingIntent,null,null);
}
   
   @Override
   public  void onPause() {
    //Onpause stop listening
    if (nfcAdapter != null) {
        nfcAdapter.disableForegroundDispatch((Activity)context);
    }
}

public boolean resolveIntent(Intent intent) {
	// if(true)throw new RuntimeException("RISOLTO!!");
    String action = intent.getAction();
    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
            || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) 
			{
            Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            assert tag != null;
            // byte[] payload = 
			// String ss = detectTagData(tag);
			 // Toast.makeText(null,ss,Toast.LENGTH_LONG).show();
			 if(onTag!=null) onTag.accept(tag.getId());
			 return true;
        }
	return false;
}


	public Object exec(String command, Object params) {
		System.out.println("Registering NFC TAG");
		if(command.equals("REGISTER"))
		{
			onTag = (Consumer<byte[]>) params;
		}
		return true;
	}

   
}
