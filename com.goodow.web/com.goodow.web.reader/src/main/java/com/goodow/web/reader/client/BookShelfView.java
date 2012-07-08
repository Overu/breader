package com.goodow.web.reader.client;

import com.goodow.web.reader.client.FileDisplay.Presenter;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.file.DirectoryEntry;
import com.googlecode.gwtphonegap.client.file.DirectoryReader;
import com.googlecode.gwtphonegap.client.file.EntryBase;
import com.googlecode.gwtphonegap.client.file.FileCallback;
import com.googlecode.gwtphonegap.client.file.FileEntry;
import com.googlecode.gwtphonegap.client.file.FileError;
import com.googlecode.gwtphonegap.client.file.FileReader;
import com.googlecode.gwtphonegap.client.file.FileSystem;
import com.googlecode.gwtphonegap.client.file.FileWriter;
import com.googlecode.gwtphonegap.client.file.Flags;
import com.googlecode.gwtphonegap.client.file.ReaderCallback;
import com.googlecode.gwtphonegap.client.file.WriterCallback;
import com.googlecode.gwtphonegap.collection.shared.LightArray;

import java.util.LinkedList;

public class BookShelfView extends BookListView implements Presenter {

  private LightArray<EntryBase> currentEntries;

  private DirectoryEntry currentDir;

  private FileEntry currentFile;

  private final FileDisplay display;

  private final PhoneGap phoneGap;

  @Inject
  public BookShelfView(final PhoneGap phoneGap, final FileDisplay display) {

    this.phoneGap = phoneGap;
    this.display = display;
    rightButton.setText("书城");

    scrollPanel.setSnap(true);
    scrollPanel.setMomentum(false);
    scrollPanel.setShowScrollBarX(false);
    scrollPanel.setShowScrollBarY(false);
    scrollPanel.setScrollingEnabledX(false);
    scrollPanel.setScrollingEnabledY(true);
    scrollPanel.setAutoHandleResize(true);
    scrollPanel.setWidget(display);

    phoneGap.getFile().requestFileSystem(FileSystem.LocalFileSystem_APPLICATION, 0,
        new FileCallback<FileSystem, FileError>() {

          @Override
          public void onFailure(final FileError error) {
            display.getStatus().setHTML(
                "Failed to request file system with error code: " + error.getErrorCode());
          }

          @Override
          public void onSuccess(final FileSystem entry) {
            gotFileSystem(entry);
          }
        });
  }

  @Override
  public void createFile(final String fileName) {
    if (currentDir != null) {

      currentDir.getFile(fileName, new Flags(true, false),
          new FileCallback<FileEntry, FileError>() {

            @Override
            public void onFailure(final FileError error) {
              Window.alert("error");

            }

            @Override
            public void onSuccess(final FileEntry entry) {
              listDirectory(currentDir);

            }
          });
    }

  }

  @Override
  public void onActionButtonPressed() {
    display.showSelectMenu();

  }

  @Override
  public void onBackButtonPressed() {
  }

  @Override
  public void onEntrySelected(int index) {
    if (index == 0) {
      if (currentDir != null) {
        currentDir.getParent(new FileCallback<DirectoryEntry, FileError>() {

          @Override
          public void onFailure(final FileError error) {
            Window.alert("error und so !!!!!!!!");

          }

          @Override
          public void onSuccess(final DirectoryEntry entry) {
            listDirectory(entry);

          }
        });
      }
      display.getFileContent().setText("");
      return;
    }
    index = index - 1;
    EntryBase entryBase = currentEntries.get(index);

    if (entryBase.isDirectory()) {
      DirectoryEntry directoryEntry = entryBase.getAsDirectoryEntry();
      listDirectory(directoryEntry);
      display.getFileContent().setText("");
      return;
    }

    if (entryBase.isFile()) {
      FileEntry fileEntry = entryBase.getAsFileEntry();
      readFile(fileEntry);
      display.setSelected(index + 1);
      return;
    }

  }

  @Override
  public void overWriteFile() {
    if (display.confirm("Really rewrite file?")) {
      if (currentFile != null) {
        currentFile.createWriter(new FileCallback<FileWriter, FileError>() {

          @Override
          public void onFailure(final FileError error) {
            Window.alert("can not create writer");

          }

          @Override
          public void onSuccess(final FileWriter entry) {
            entry.setOnWriteEndCallback(new WriterCallback<FileWriter>() {

              @Override
              public void onCallback(final FileWriter result) {
                Window.alert("file written");

              }
            });

            entry.setOnErrorCallback(new WriterCallback<FileWriter>() {

              @Override
              public void onCallback(final FileWriter result) {
                Window.alert("error while writing file");

              }
            });
            entry.write(display.getFileContent().getText());

          }
        });
      }
    }

  }

  @Override
  protected String getRightLink() {
    return "/bookstore";
  }

  private void gotFileSystem(final FileSystem fileSystem) {
    listDirectory(fileSystem.getRoot());
  }

  private void listDirectory(final DirectoryEntry directoryEntry) {
    currentDir = directoryEntry;

    display.getStatus().setHTML("Listing '" + directoryEntry.getName() + "");
    DirectoryReader directoryReader = directoryEntry.createReader();

    directoryReader.readEntries(new FileCallback<LightArray<EntryBase>, FileError>() {

      @Override
      public void onFailure(final FileError error) {
        display.getStatus().setHTML("Error while listing '" + directoryEntry.getFullPath() + "'");

      }

      @Override
      public void onSuccess(final LightArray<EntryBase> entries) {
        currentEntries = entries;
        display.getStatus().setHTML("Directory '" + directoryEntry.getFullPath() + "'");

        LinkedList<FileDemo> list = new LinkedList<FileDemo>();
        list.add(new FileDemo("../"));

        for (int i = 0; i < entries.length(); i++) {
          EntryBase entryBase = entries.get(i);
          String name;

          if (entryBase.isDirectory()) {
            DirectoryEntry directoryEntry = entryBase.getAsDirectoryEntry();
            name = directoryEntry.getName();
            // display.addEntry(name, true);
            // display.getEntry(i).addClickHandler(new
            // DirectoryClickHandler(i));
          } else {
            FileEntry fileEntry = entryBase.getAsFileEntry();
            name = fileEntry.getName();
            // display.addEntry(name, false);
            // display.getEntry(i).addClickHandler(new
            // FileClickHandler(i));
          }

          list.add(new FileDemo(name));

        }

        display.render(list);

      }
    });
  }

  private void readFile(final FileEntry fileEntry) {
    FileReader reader = phoneGap.getFile().createReader();

    reader.setOnloadCallback(new ReaderCallback<FileReader>() {

      @Override
      public void onCallback(final FileReader result) {

        display.getFileContent().setText(result.getResult());
        currentFile = fileEntry;
      }
    });

    reader.setOnErrorCallback(new ReaderCallback<FileReader>() {

      @Override
      public void onCallback(final FileReader result) {
        display.getFileContent().setText("Error while reading file");

      }
    });

    reader.readAsText(fileEntry);
  }
}
