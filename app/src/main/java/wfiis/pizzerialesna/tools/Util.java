package wfiis.pizzerialesna.tools;

import android.content.pm.PackageManager;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.widget.TextView;

import com.inverce.mod.core.IM;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Collator;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class Util {
    public static boolean isApi(int level) {
        return android.os.Build.VERSION.SDK_INT >= level;
    }

    public static <T> T or(T value, T orDefault) {
        return value == null ? orDefault : value;
    }

    @SafeVarargs
    public static <T> T firstNonNull(T... elements) {
        for (T t : elements) {
            if (t != null) return t;
        }
        return null;
    }

    public static String decimPlace(String data, int count) {
        try {
            return decimPlace(Double.parseDouble(data), count);
        } catch (Exception ex) {
            return data;
        }
    }

    public static String decimPlace(double val, int count) {
        try {
            String format = "0.00";
            switch (count) {
                case 0:
                    format = "0";
                    break;
                case 1:
                    format = "0.0";
                    if (val == Math.round(val)) {
                        return String.valueOf((int) Math.round(val));
                    }
                    break;
                case 2:
                    format = "0.00";
                    break;
                case 3:
                    format = "0.000";
                    break;
                case 4:
                    format = "0.0000";
                    break;
            }


            return new DecimalFormat(format).format(val).replace(".", ",");
        } catch (Exception ex) {
            return String.valueOf(val);
        }
    }

    public static String toJsonName(String org) {
        if (org == null) {
            org = "unknown";
        }
        String s = org.toLowerCase().replaceAll(" ", "_");
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    public static Double parseDouble(String data) {
        try {
            return Double.parseDouble(data);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static String[] split(String toSplit) {

        if (toSplit != null) {
            try {
                return toSplit.split(",");
            } catch (Exception ignored) {
                /* ignored */
            }
        }
        return new String[]{};
    }

    public static String toBase64(String input) {
        return input == null ? null : Base64.encodeToString(input.getBytes(), Base64.NO_WRAP);
    }

    public static String fromBase64(String input) {
        return input == null ? null : new String(Base64.decode(input, Base64.NO_WRAP));
    }

    public static String getAppVersion() {
        try {
            PackageManager m = IM.context().getPackageManager();
            return m.getPackageInfo(IM.context().getPackageName(), 0).versionName;
        } catch (Exception ignored) {
            return "1.0";
        }
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);

            // Transfer bytes find in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } finally {
            closeSilently(in);
            closeSilently(out);
        }
    }

    public static void closeSilently(Closeable target) {
        try {
            if (target != null)
                target.close();
        } catch (Exception ignored) {
        }
    }

    public static String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    public static <T> T defaultVal(T val, @NonNull T onNull) {
        return val != null ? val : onNull;
    }

    public static boolean checkRange(int pos, java.util.List<?> list) {
        return list != null && pos >= 0 && pos < list.size();
    }

    public static String asLineArray(List<?> list) {
        if (list == null) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            out.append("\t").append(i).append(". ").append(o != null ? o.toString() : "[null]").append("\n");
        }
        return out.toString();
    }

    public static boolean nullOrEmpty(String txt) {
        return txt == null || txt.length() < 1;
    }

    public static <T> boolean nullOrEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    public static String emptyToNull(String value) {
        if (value != null && value.length() < 1) {
            return null;
        } else {
            return value;
        }
    }

    public static int compare(String n1, String n2) {
        if (n1 == n2) return 0;
        if (n1 == null) return -1;
        if (n2 == null) return 1;
        return n1.compareToIgnoreCase(n2);
    }

    public static int comparePolish(String n1, String n2) {
        if (n1 == n2) return 0;
        if (n1 == null) return -1;
        if (n2 == null) return 1;

        Collator collator = Collator.getInstance(new Locale("pl", "PL"));
        return collator.compare(n1, n2);
    }

    public static <T> List<T> orEmptyList(List<T> list) {
        return list != null ? list : new ArrayList<T>();
    }

    public static <T> boolean safeEquals(T a, T b) {
        return a == b || a != null && a.equals(b);
    }

    public static void setTextColor(TextView textView, @ColorRes int color) {
        int colorInt;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            colorInt = IM.application().getColor(color);
        } else {
            colorInt = IM.application().getResources().getColor(color);
        }
        textView.setTextColor(colorInt);
    }

    public static int getColor(@ColorRes int colorId) {
        return ActivityCompat.getColor(IM.context(), colorId);
    }

    public static long toEndOfDay(long timestampMs) {
        GregorianCalendar day = new GregorianCalendar(TimeZone.getDefault(), Locale.getDefault());
        day.setTime(new Date(timestampMs));
        day.set(GregorianCalendar.HOUR, 23);
        day.set(GregorianCalendar.MINUTE, 23);
        day.set(GregorianCalendar.SECOND, 23);
        return day.getTimeInMillis();
    }

    public static Map<String, String> createMapForDynamicCategories(String rootCategory, List<String> list, @Nullable String tree) {
        Map<String, String> params = new LinkedHashMap<>();
        if (nullOrEmpty(list)) {
            params.put("fq[category_id]", rootCategory);
        } else {
            params.put("fq[category_id][0]", rootCategory);
            for (int i = 0; i < list.size(); i++) {
                String key = "fq[category_id][" + (i + 1) + "]";
                params.put(key, list.get(i));
            }
        }
        if (tree != null) {
            params.put("tree", tree);
        }

        return params;
    }
}