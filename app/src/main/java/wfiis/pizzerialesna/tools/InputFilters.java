package wfiis.pizzerialesna.tools;

import android.text.InputFilter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class InputFilters extends ArrayList<InputFilter> {

    public InputFilters addCharacterLimitFilter(int inputLength) {
        this.add(new InputFilter.LengthFilter(inputLength));
        return this;
    }

    public InputFilters addCharactersRemovalFilter(CharSequence ... charSequences) {
        return addCharactersRemovalFilter(asList(charSequences));
    }

    public InputFilters addCharactersRemovalFilter(List<CharSequence> charSequences) {
        this.add((source, start, end, dest, dstart, dend) -> charSequences.contains(source) ? "" : null);
        return this;
    }
}

