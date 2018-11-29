package sumsar1812.github.io.todonearme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;
    public PagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {
            return new CurrentToDoFragment();
        } else if (i == 1) {
            return new HistoryFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
