package sw.superwhateverjnr.texture;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import android.graphics.Bitmap;

@Getter
@Setter
@AllArgsConstructor(suppressConstructorProperties=true)
@ToString
@EqualsAndHashCode
public class Texture
{
	private int width, height;
	private Bitmap image;
}
