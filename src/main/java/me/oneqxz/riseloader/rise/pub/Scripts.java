package me.oneqxz.riseloader.rise.pub;

import javafx.application.Platform;
import me.oneqxz.riseloader.RiseUI;
import me.oneqxz.riseloader.fxml.components.impl.ErrorBox;
import me.oneqxz.riseloader.rise.pub.interfaces.IFileData;
import me.oneqxz.riseloader.rise.pub.interfaces.IPublic;
import me.oneqxz.riseloader.rise.pub.interfaces.IPublicData;
import me.oneqxz.riseloader.utils.requests.Requests;
import me.oneqxz.riseloader.utils.requests.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

public class Scripts implements IPublic {

    private IPublicData[] data;

    @Override
    public @NotNull IPublicData[] getData() {
        return data;
    }

    @Override
    public IPublic updateData() {
        try
        {
            Response resp = Requests.get(RiseUI.serverIp + "/scripts/");
            if(resp.getStatusCode() == 200)
            {
                JSONObject json = resp.getJSON();
                IPublicData[] scriptsData = new IPublicData[json.getInt("size")];

                JSONArray scripts = json.getJSONArray("scripts");

                for(int i = 0; i < scripts.length(); i++)
                {
                    JSONObject script = scripts.getJSONObject(i);
                    scriptsData[i] = new IPublicData() {
                        public @NotNull String getDescription() {
                            return script.getString("description");
                        }
                        public @NotNull IFileData getFileData() {
                            return new IFileData() {
                                public @NotNull String getDownloadURL() {
                                    return script.getJSONObject("download").getString("file");
                                }
                                public @NotNull String getMD5() {
                                    return script.getJSONObject("download").getString("md5");
                                }
                                public long created() {
                                    return script.getJSONObject("download").getLong("created");
                                }
                            };
                        }
                        public @NotNull String getName() {
                            return script.getString("name");
                        }

                        public @NotNull String getServers() {
                            return script.getString("servers");
                        }
                        public @Nullable String getPreviewURL() {
                            return script.optString("preview", null);
                        }
                        public @Nullable String getVideoURL() {
                            return script.optString("video", null);
                        }
                    };
                }

                this.data = scriptsData;
            }
            else
                throw new IllegalStateException(RiseUI.serverIp + "/scripts/ returned invalid status code, " + resp.getStatusCode());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Platform.runLater(() -> new ErrorBox().show(e));
        }

        return this;
    }
}
