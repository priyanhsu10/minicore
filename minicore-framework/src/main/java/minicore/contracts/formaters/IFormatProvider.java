package minicore.contracts.formaters;

import minicore.contracts.HttpContext;

public interface IFormatProvider {
boolean canSuportedInputMediaType(String mediaType);
  boolean canSuportedOutpMediaType(String mediaType);
  IInputFormatter getSuportedInputFormatter(HttpContext context);
  IOutputFormatter getSupportedOutputFormatter(HttpContext context);
}
