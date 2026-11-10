package application;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AllSongsController {

    @FXML
    private ListView<Song> songsList;
    @FXML 
    private TextField SearchField;
    @FXML
    private ComboBox<String> dropDown = new ComboBox<>();
    
    @FXML
    public void initialize() {
        MusicLibrary.loadSongs();
        songsList.getItems().setAll(MusicLibrary.getSongs().toList());
        dropDown.getItems().addAll("Linear Search","Binary Search");
        dropDown.setPromptText("Choose Search Algorithm");
        songsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                playSelected();
            }
        });
    }

    @FXML
    private void SortSongs(ActionEvent event) {

        List<Song> s = new ArrayList<>(songsList.getItems());

        MusicLibrary.radixSort(s);

        songsList.getItems().setAll(s);
    }
    
    @SuppressWarnings("unused")
	@FXML
	private void BinarySearch() {
    	if(SearchField.getText()==null) {
    		return;
    	}
    	if(songsList.getItems() == null) {
    		return;
    	}
    	String Word = SearchField.getText().toLowerCase();
    	int low = 0;
    	int high = songsList.getItems().size() - 1;
        List<Song> s = new ArrayList<>(songsList.getItems());
        MusicLibrary.radixSort(s);
        ArrayList<Song> ans = new ArrayList<>();
    	while(low<=high) {
    	int mid = low + (high-low)/2;
    	String value = s.get(mid).getTitle().toLowerCase();
    	if(value.contains(Word)) {
    		ans.add(s.get(mid));
    		break;
    	}
    	if(value.compareTo(Word) < 0) {
    		low = mid + 1;
    	}
    	else {
    		high = mid - 1;
    	}
    	}
    	if(ans!=null) {
            songsList.getItems().setAll(ans);
         }
    }
    
    @SuppressWarnings("unused")
	@FXML
    private void LinearSearch() {
    	if(SearchField.getText()==null) {
    		return;
    	}
    	if(songsList.getItems() == null) {
    		return;
    	}
    	String word = SearchField.getText().toLowerCase().trim();
        List<Song> s = new ArrayList<>(songsList.getItems());
        List<Song> res = new ArrayList<>();
        for(Song song : s) {
        	String songTitle = song.getTitle();
        	if(songTitle.toLowerCase().contains(word) && songTitle!=null) {
        		res.add(song);
        	}
        }
        if(res!=null) {
        songsList.getItems().setAll(res);
        }
    }
    
    
    @FXML
    private void SearchAlgorithm() {
        if (dropDown.isDisable()) {
            return;
        }
        String choice = dropDown.getValue();

        if ("Linear Search".equals(choice)) {
            LinearSearch();
        }
        else if ("Binary Search".equals(choice)) {
            BinarySearch();
        }
    }
    
    @FXML
    private void onPlaySelected(ActionEvent event) {
        playSelected();
    }

    @FXML
    private void onBack(ActionEvent event) {
        Navigator.show(event, "Choice.fxml");
    }

    private void playSelected() {
        Song selected = songsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        MusicPlayerController player = Navigator.show(songsList, "MusicPlayer.fxml");
        player.start(selected, MusicLibrary.getSongs().toList(), "AllSongs.fxml");
    }
}