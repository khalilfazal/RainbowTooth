package com.rainbowtooth;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * An empty view.
 * Used to apply themes to
 * 
 * @author Khalil Fazal
 */
public class EmptyView extends View {

    /**
     * Needed to instantiate from code.
     * 
     * @param ctx Where the view is running in, through which it can access the current theme, resources, etc.
     */
    public EmptyView(final Context ctx) {
        this(ctx, null, 0);
    }

    /**
     * Needed to be creatable from XML.
     * 
     * @param ctx Where the view is running in, through which it can access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    public EmptyView(final Context ctx, final AttributeSet attrs) {
        this(ctx, attrs, 0);
    }

    /**
     * Needed to be creatable from XML.
     * 
     * @param ctx Where the view is running in, through which it can access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyle The default style to apply to this view.
     *     If 0, no style will be applied (beyond what is included in the theme).
     *     This may either be an attribute resource, whose value will be retrieved from the current theme, or an explicit style resource.
     */
    public EmptyView(final Context ctx, final AttributeSet attrs, final int defStyle) {
        super(ctx, attrs, defStyle);
    }

}
