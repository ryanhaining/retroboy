package se.embargo.onebit.rs;

import se.embargo.onebit.R;
import se.embargo.onebit.filter.ScriptC_atkinson;
import se.embargo.onebit.filter.ScriptC_mono;
import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

public class AtkinsonScript implements IBitmapFilter {
	private RenderScript _renderContext;
	private ScriptC_atkinson _filter;
	private ScriptC_mono _mono;

	public AtkinsonScript(Context context) {
        _renderContext = RenderScript.create(context);
        _filter = new ScriptC_atkinson(_renderContext, context.getResources(), R.raw.atkinson);
        _mono = new ScriptC_mono(_renderContext, context.getResources(), R.raw.mono);
	}
	
	@Override
	public Bitmap apply(Bitmap input) {
        Allocation imagebuf = Allocation.createFromBitmap(
        	_renderContext, input, 
        	Allocation.MipmapControl.MIPMAP_NONE, 
        	Allocation.USAGE_SCRIPT);

        _filter.set_gIn(imagebuf);
        _filter.set_gOut(imagebuf);
        _filter.set_gMonoScript(_mono);
        _filter.invoke_filter();
        
        // Reuse the input bitmap
        imagebuf.copyTo(input);
        return input;
	}
}
