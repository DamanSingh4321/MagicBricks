package gurpreetsk.me.magicbricks;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {

    EditText msgET;
    Button btn;
    TextView inputTV, replyTV;
    String msg, reply;
    LinearLayout linearLayout;

    private String TAG = "MainActivity";

    public Hashtable<String, Integer> city = new Hashtable<>();
    public Hashtable<String, Integer> locality = new Hashtable<>();
    public Hashtable< Integer, String> cityInv = new Hashtable<>();
    public Hashtable<Integer, String> localityInv = new Hashtable<>();
    private ArrayList<String> cities, locations, roomSize, budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getHandles();
        makeDS();

        TextView firstTV = new TextView(MainActivity.this);
        firstTV.setGravity(Gravity.CENTER);
        firstTV.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        firstTV.setTextColor(Color.parseColor("#FF4081"));
        firstTV.setTextSize(16);
        firstTV.setText("Welcome to PropertyBot\n Enter hi or query to start");
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(firstTV);

        btn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {

                LinearLayout.LayoutParams layoutParamsSender = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParamsSender.setMargins(100, 10, 50, 10);
                linearLayout.setOrientation(LinearLayout.VERTICAL);     //Internal LinearLayout

                //WHAT USER SAYS
                msg = msgET.getText().toString();
                inputTV = new TextView(MainActivity.this);
                linearLayout.addView(inputTV, layoutParamsSender);
                inputTV.setGravity(Gravity.END);
                inputTV.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                inputTV.setTextSize(16);
                inputTV.setTextColor(Color.parseColor("#0277BD"));         //Use #558B2F for bot reply
                inputTV.setText(msg);
                msgET.setText("");

                //WHAT WE SAY
                LinearLayout.LayoutParams layoutParamsBot = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParamsBot.setMarginStart(15);

                if (msg.length() < 15) {
                    msg = msg.toLowerCase();
                    replyTV = new TextView(MainActivity.this);
                    reply = giveReply(msg);
                    replyTV.setText(reply);
                    checkMessage(msg.toLowerCase());
                    replyTV.setTextSize(16);
                    replyTV.setGravity(Gravity.LEFT);
                    replyTV.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
                    replyTV.setTextColor(Color.parseColor("#558B2F"));
                    linearLayout.addView(replyTV, layoutParamsBot);
                } else {

                    final Intent intent = new Intent(MainActivity.this, MapsActivity.class);

                    replyTV = new TextView(MainActivity.this);
                    linearLayout.addView(replyTV, layoutParamsSender);
                    replyTV.setGravity(Gravity.START);
                    replyTV.setTextSize(16);
                    replyTV.setTextColor(Color.parseColor("#558B2F"));         //Use #558B2F for bot reply
                    replyTV.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
//                    replyTV.setText(msg);

                    String locationNeeded = "", cityNeeded = "";
                    String budgetneeded = "", sizeNeeded = "";

                    msg = msg.toLowerCase();

                    for (String cty : cities) {
                        if (msg.contains(cty)) {
                            cityNeeded = city.get(cty).toString();
                        }
                    }
                    for (String location : locations) {
                        if (msg.contains(location)) {
                            locationNeeded = locality.get(location).toString();
                        }
                    }
                    for (String bud : budget) {
                        if (msg.contains(bud)) {
                            budgetneeded = bud;
                        }
                    }
                    for (String size : roomSize) {
                        if (msg.contains(size)) {
                            sizeNeeded = size;
                        }
                    }

                    replyTV = new TextView(MainActivity.this);
                    //reply = giveReply(msg);

                    String needed;
                    String loc, siz, bud;
                    String CITY = city.get(Splash.tellCity()).toString();       //TODO: DIKKAT

                    String replyString = "Please enter some data.";
                    if (cityNeeded.equals("") && locationNeeded.equals("") && budgetneeded.equals("") && sizeNeeded.equals("")) {
                        reply = replyString;
                        intent.putExtra("NEED", "nothing");
                        intent.putExtra("SENT", "nothing");
                        intent.putExtra("BOTcity", CITY);
                    }

                    else if (cityNeeded.equals("") && !locationNeeded.equals("") && budgetneeded.equals("") && sizeNeeded.equals("")) {
                        reply = "Finding houses in locality " + localityInv.get(Integer.parseInt(locationNeeded));
                        needed = "location";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("SENT", locationNeeded);
                        intent.putExtra("BOTcity", CITY);
                    }else if (!cityNeeded.equals("") && locationNeeded.equals("") && budgetneeded.equals("") && sizeNeeded.equals("")) {
                        reply = "Finding houses in city " + cityInv.get(Integer.parseInt(cityNeeded));
                        needed = "city";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("SENT", cityNeeded);
                        intent.putExtra("BOTcity", cityNeeded);
                    } else if (cityNeeded.equals("") && locationNeeded.equals("") && !budgetneeded.equals("") && sizeNeeded.equals("")) {
                        reply = "Finding houses in budget " + budgetneeded;
                        needed = "budget";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("BOTcity", CITY);
                        intent.putExtra("SENT", budgetneeded);
                    } else if (cityNeeded.equals("") && locationNeeded.equals("") && budgetneeded.equals("") && !sizeNeeded.equals("")) {
                        reply = "Finding houses of size " + sizeNeeded;
                        needed = "size";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("SENT", sizeNeeded);
                        intent.putExtra("BOTcity", CITY);
                    }

                    else if (!cityNeeded.equals("") && !locationNeeded.equals("") && sizeNeeded.equals("") && budgetneeded.equals("")) {
                        reply = "Finding houses in city " + cityInv.get(Integer.parseInt(cityNeeded)) + " in locality " + localityInv.get(Integer.parseInt(locationNeeded));
                        needed = "city and location";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("BOTcity", cityNeeded);
                        intent.putExtra("SENTLOC", locationNeeded);
                    }else if (cityNeeded.equals("") && !locationNeeded.equals("") && !sizeNeeded.equals("") && budgetneeded.equals("")) {
                        reply = "Finding houses in locality " + localityInv.get(Integer.parseInt(locationNeeded)) + " of size " + sizeNeeded;
                        needed = "location and size";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("SENTLOC", locationNeeded);
                        intent.putExtra("SENTSIZ", sizeNeeded);
                        intent.putExtra("BOTcity", CITY);
                    } else if (cityNeeded.equals("") && locationNeeded.equals("") && !sizeNeeded.equals("") && !budgetneeded.equals("")) {
                        reply = "Finding houses of size " + sizeNeeded + " in budget " + budgetneeded;
                        needed = "size and budget";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("SENTSIZ", sizeNeeded);
                        intent.putExtra("SENTBUD", budgetneeded);
                        intent.putExtra("BOTcity", CITY);
                    }else if (!cityNeeded.equals("") && locationNeeded.equals("") && !sizeNeeded.equals("") && budgetneeded.equals("")) {
                        reply = "Finding houses in city " + cityInv.get(Integer.parseInt(cityNeeded)) + " of size " + sizeNeeded;
                        needed = "city and size";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("BOTcity", cityNeeded);
                        intent.putExtra("SENTSIZ", sizeNeeded);
                    }  else if (cityNeeded.equals("") && !locationNeeded.equals("") && sizeNeeded.equals("") && !budgetneeded.equals("")) {
                        reply = "Finding houses in locality " + localityInv.get(Integer.parseInt(locationNeeded)) + " in budget " + budgetneeded;
                        needed = "location and budget";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("SENTLOC", locationNeeded);
                        intent.putExtra("SENTBUD", budgetneeded);
                        intent.putExtra("BOTcity", CITY);
                    }else if (!cityNeeded.equals("") && locationNeeded.equals("") && sizeNeeded.equals("") && !budgetneeded.equals("")) {
                        reply = "Finding houses in city " + cityInv.get(Integer.parseInt(cityNeeded)) + " in budget " + budgetneeded;
                        needed = "city and budget";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("BOTcity", cityNeeded);
                        intent.putExtra("SENTBUD", budgetneeded);
                    }

                    else if (!cityNeeded.equals("") && !locationNeeded.equals("") && !sizeNeeded.equals("") && budgetneeded.equals("")) {
                        reply = "Finding houses in city " + cityInv.get(Integer.parseInt(cityNeeded)) + " in budget " + budgetneeded +" of size "+sizeNeeded;
                        needed = "city size location";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("BOTcity", cityNeeded);
                        intent.putExtra("SENTBUD", budgetneeded);
                        intent.putExtra("SENTSIZ", sizeNeeded);
                    }else if (!cityNeeded.equals("") && !locationNeeded.equals("") && sizeNeeded.equals("") && !budgetneeded.equals("")) {
                        reply = "Finding houses in city " + cityInv.get(Integer.parseInt(cityNeeded)) + " in locality "+ localityInv.get(Integer.parseInt(locationNeeded)) +" in budget " + budgetneeded;
                        needed = "city location budget";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("BOTcity", cityNeeded);
                        intent.putExtra("SENTLOC", locationNeeded);
                        intent.putExtra("SENTSIZ", budgetneeded);
                    }else if (!cityNeeded.equals("") && locationNeeded.equals("") && !sizeNeeded.equals("") && !budgetneeded.equals("")) {
                        reply = "Finding houses in city " + cityInv.get(Integer.parseInt(cityNeeded)) + " of size "+ sizeNeeded +" in budget " + budgetneeded;
                        needed = "city budget size";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("BOTcity", cityNeeded);
                        intent.putExtra("SENTBUD", budgetneeded);
                        intent.putExtra("SENTSIZ", sizeNeeded);
                    }else if (cityNeeded.equals("") && !locationNeeded.equals("") && sizeNeeded.equals("") && !budgetneeded.equals("")) {
                        reply = "Finding houses of size " + sizeNeeded + " at location " + localityInv.get(Integer.parseInt(locationNeeded)) +" in budget " + budgetneeded;
                        needed = "location budget size";
                        intent.putExtra("NEED", needed);
                        intent.putExtra("SENTLOC", locationNeeded);
                        intent.putExtra("SENTSIZ", sizeNeeded);
                        intent.putExtra("SENTBUD", budgetneeded);
                        intent.putExtra("BOTcity", CITY);
                    }


                    else if (!cityNeeded.equals("") && !locationNeeded.equals("") && !sizeNeeded.equals("") && !budgetneeded.equals("")) {              //MAIN FOCUS HERE
                        reply = "Finding houses in " + cityInv.get(Integer.parseInt(cityNeeded)) + " locality " + localityInv.get(Integer.parseInt(locationNeeded)) + " of size " + sizeNeeded + " in budget " + budgetneeded;
                        needed = "all";
                        loc = locationNeeded;
                        siz = sizeNeeded;
                        bud = budgetneeded;
                        intent.putExtra("NEED", needed);
                        intent.putExtra("SENT", "all");
                        intent.putExtra("CITY", cityNeeded);
                        intent.putExtra("LOC", loc);
                        intent.putExtra("SIZ", siz);
                        intent.putExtra("BUD", bud);
                        intent.putExtra("BOTcity", cityNeeded);
                    }

                    //locationNeeded, sizeNeeded, budgetNeeded to be send

                    replyTV.setText(reply);
                    replyTV.setTextSize(16);
                    //replyTV.setBackgroundColor(Color.parseColor("#F0F4C3"));
                    replyTV.setGravity(Gravity.LEFT);
                    replyTV.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);
                    replyTV.setTextColor(Color.parseColor("#558B2F"));
                    linearLayout.addView(replyTV, layoutParamsBot);

                    if(!intent.getExtras().get("NEED").equals("nothing")) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Found results")
                                        .setMessage("Click to go to the results")
                                        .setPositiveButton("GO", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
//                                        checkMessage(msg.toLowerCase());
                                                intent.putExtra("EXTRA", "SUGGEST");
                                                startActivity(intent);
//                                                Toast.makeText(MainActivity.this, "start intent", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_menu_view)
                                        .show();
                            }
                        }, 2000);
                    }
                }

            }
        });


    }

    private void makeDS() {

        createCityArrayList();
        createLocationsArrayList();
        createBudgetArrayList();
        createRoomArrayList();
        createLocalityHashtable();
        createCityHashtable();
        getCityFromKey();
        getLocalityFromKey();

    }

    private void getHandles() {
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        msgET = (EditText) findViewById(R.id.msgET);
        btn = (Button) findViewById(R.id.sendBtn);
    }

    void createBudgetArrayList() {
        budget = new ArrayList<>();
        budget.add("5000");
        budget.add("10000");
        budget.add("15000");
        budget.add("20000");
        budget.add("upto 5000");
        budget.add("about 5000");
        budget.add("5000-10000");
        budget.add("b/w 5000-10000");
        budget.add("b/w 10000-20000");
        budget.add("10000-20000");
        budget.add("about 20000");
    }

    void createRoomArrayList() {
        roomSize = new ArrayList<>();
        roomSize.add("1bhk");
        roomSize.add("2bhk");
        roomSize.add("3bhk");
        roomSize.add("1 bhk");
        roomSize.add("2 bhk");
        roomSize.add("3 bhk");
        //roomSize.add("");
    }

    void createCityArrayList() {
        cities = new ArrayList<>();
        cities.add("hyderabad");
        cities.add("delhi");
        cities.add("ahmedabad");
        cities.add("gurgaon");
        cities.add("bangalore");
        cities.add("mumbai");
        cities.add("pune");
        cities.add("kolkata");
        cities.add("noida");
    }

    void createLocationsArrayList() {
        locations = new ArrayList<>();
        locations.add("miyapur");
        locations.add("adibatla");
        locations.add("subhash");
        locations.add("sangam");
        locations.add("panchvati");
        locations.add("prahlad");
        locations.add("palam");
        locations.add("sector");
        locations.add("nelamangala");
        locations.add("sarjapur");
        locations.add("bandra");
        locations.add("malad");
        locations.add("kalyani");
        locations.add("viman");
        locations.add("rajarhat");
        locations.add("thakur");
        locations.add("eta");
    }

    void createCityHashtable() {
        city.put("hyderabad", 2060);
        city.put("delhi", 2624);
        city.put("ahmedabad", 2690);
        city.put("gurgaon", 2951);
        city.put("bangalore", 3327);
        city.put("mumbai", 4320);
        city.put("pune", 4378);
        city.put("kolkata", 6903);
        city.put("noida", 7045);
    }

    void createLocalityHashtable() {
        locality.put("miyapur", 80185);
        locality.put("adibatla", 85822);
        locality.put("subhash", 53514);
        locality.put("sangam", 78193);
        locality.put("panchvati", 54469);
        locality.put("prahlad", 84435);
        locality.put("palam", 78710);
        locality.put("sector", 86514);
        locality.put("nelamangala", 80455);
        locality.put("sarjapur", 80060);
        locality.put("bandra", 78839);
        locality.put("malad", 80084);
        locality.put("kalyani", 82343);
        locality.put("viman", 79726);
        locality.put("rajarhat", 79304);
        locality.put("thakur", 84834);
        locality.put("eta", 98493);
//        locality.put("noida", 93788);     both in city and locality
    }

    void getCityFromKey() {
        cityInv.put(2060, "hyderabad");
        cityInv.put(2624, "delhi");
        cityInv.put(2690, "ahmedabad");
        cityInv.put(2951, "gurgaon");
        cityInv.put(3327, "bangalore");
        cityInv.put(4320, "mumbai");
        cityInv.put(4378, "pune");
        cityInv.put(6903, "kolkata");
        cityInv.put(7045, "noida");
    }

    void getLocalityFromKey() {
        localityInv.put(80185, "miyapur");
        localityInv.put(85822, "adibatla");
        localityInv.put(53514, "subhash");
        localityInv.put(78193, "sangam");
        localityInv.put(54469, "panchvati");
        localityInv.put(84435, "prahlad");
        localityInv.put(78710, "palam");
        localityInv.put(86514, "sector");
        localityInv.put(80455, "nelamangala");
        localityInv.put(80060, "sarjapur");
        localityInv.put(78839, "bandra");
        localityInv.put(80084, "malad");
        localityInv.put(82343, "kalyani");
        localityInv.put(79726, "viman");
        localityInv.put(79304, "rajarhat");
        localityInv.put(84834, "thakur");
        localityInv.put(98493, "eta");
//        locality.put("noida", 93788);     both in city and locality
    }

    private void checkMessage(String msg) {             //FOR Hi related

        String CITY = city.get(Splash.tellCity()).toString();
        Log.v(TAG, CITY);
//        String CITY = "7045";

        switch (msg) {
//            case CITY:

            case "1":
            case "1.":
            case "location": {
//            Toast.makeText(MainActivity.this, "Location" + CITY, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("what", "location");
                intent.putExtra("BOTmsg", "location");
                intent.putExtra("BOTcity", CITY);
                startActivity(intent);
                break;
            }
            case "a":
            case "b":
            case "c":
            case "d":
            case "5000":
            case "<5000":
            case "under 5000":
            case "5000-10000":
            case "b/w 5000-10000":
            case "10000-15000":
            case "b/w 10000-15000":
            case "15000-20000":
            case "b/w 15000-20000":
            case ">20000":
            case "above 20000":
            case "20000": {
                String budget = "<5000";
                if (msg.length() == 1) {
                    if (msg.equals("a")) budget = "5000";
                    if (msg.equals("b")) budget = "10000";
                    if (msg.equals("c")) budget = "15000";
                    if (msg.equals("d")) budget = "25000";
                } else if (msg.contains("5000") && msg.length() < 6) {
                    budget = "5000";
                } else if (msg.contains("10000")) {
                    budget = "10000";
                } else if (msg.contains("15000")) {
                    budget = "15000";
                }else if (msg.contains("20000")) {
                    budget = "20000";
                }else if (msg.contains("25000")) {
                    budget = "25000";
                }else if (msg.contains("30000")) {
                    budget = "30000";
                }else if (msg.contains("40000")) {
                    budget = "40000";
                }
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("what", "budget");
                intent.putExtra("BOTmsg", budget);
                intent.putExtra("BOTcity", CITY);
//                Toast.makeText(MainActivity.this, budget + CITY, Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            }
//            Toast.makeText(MainActivity.this, "Budget", Toast.LENGTH_SHORT).show();
            case "1bhk":
            case "2bhk":
            case "3bhk":
            case "1 bhk":
            case "2 bhk":
            case "3 bhk": {
//            Toast.makeText(MainActivity.this, "BHK", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                if (msg.charAt(1) == ' ') {
                    msg = msg.charAt(0) + "bhk";
                }
                intent.putExtra("wh" +
                        "at", "bhk");
                intent.putExtra("BOTmsg", msg);
                intent.putExtra("BOTcity", CITY);
//                Toast.makeText(MainActivity.this, msg + CITY, Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            }

        }
    }

    private String giveReply(String msg) {
        switch(msg) {
            case "hyderabad":
                Splash.setCity("hyderabad");
                break;
            case "delhi":
                Splash.setCity("delhi");
                break;
            case "ahmedabad":
                Splash.setCity("ahmedabad");
                break;
            case "gurgaon":
                Splash.setCity("gurgaon");
                break;
            case "bangalore":
                Splash.setCity("bangalore");
                break;
            case "mumbai":
                Splash.setCity("mumbai");
                break;
            case "pune":
                Splash.setCity("pune");
                break;
            case "kolkata":
                Splash.setCity("kolkata");
                break;
            case "noida":
                Splash.setCity("noida");
                break;
        }
        return Splash.returnData(msg);

    }

}
