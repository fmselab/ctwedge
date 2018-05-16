// this feature model is made available from the University of
// Texas at Austin by the Product Line Research Architecture
// research group at http://www.cs.utexas.edu/users/schwartz/


VncViewer : Type Base :: VncViewerMain ;

Type : Opt* :: VncViewerType;

Opt : MOpt* OptionsFeat :: OptionsMenuFeatures |
      BOpt* :: ButtonFeature |
      ClipboardFeat | RecordingFeat;

MOpt : OpEncodingFeat | OpCompressionFeat | OpJPEGqualityFeat | OpCursorShapeFeat | OpCopyRectFeat | OpRestrictedColorsFeat | OpMouse23Feat | OpViewFeat | OpShareFeat;
BOpt : AboutButtonFeat | AltTabButtonFeat | RefreshButtonFeat | CtrlAltDelButtonFeat | RecordButtonFeat | ClipboardButtonFeat | OptionsButtonFeat | DisconnectButtonFeat;

%% // non-grammar constraints

RecordButtonFeat implies RecordingFeat;
OptionsButtonFeat implies OptionsFeat;
ClipboardButtonFeat implies ClipboardFeat;

## // annotations

Type { disp="TightVNC" }
Opt { disp="Options" }
MOpt { disp="Menu Option" }
BOpt { disp="Button Option" }
OpSubFeat { disp="Hidden" }
